package com.alice.emily.module.mail;

import com.alice.emily.module.template.TemplateRender;
import com.alice.emily.utils.Errors;
import com.alice.emily.utils.LOG;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import jodd.mail.Email;
import jodd.mail.MailAddress;
import jodd.mail.SendMailSession;
import jodd.mail.SmtpServer;
import jodd.mail.SmtpSslServer;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Created by lianhao on 2017/1/9.
 */
public class MailSenderImpl implements MailSender {

    private final MailProperties configuration;
    private final TemplateRender render;
    private final SmtpServer<? extends SmtpServer> smtpServer;

    public MailSenderImpl(MailProperties configuration, TemplateRender render) {
        this.configuration = configuration;
        this.render = render;
        this.smtpServer = createSmtpServer(configuration);
    }

    private SmtpServer<? extends SmtpServer> createSmtpServer(MailProperties configuration) {
        MailProperties.MailServerProperties server = configuration.getServer();
        Preconditions.checkNotNull(server, "Mail server configuration must be provided");
        int port = server.isSsl()
                ? (server.getPort() == null ? 465 : server.getPort())
                : (server.getPort() == null ? 25 : server.getPort());
        SmtpServer<? extends SmtpServer> smtpServer = server.isSsl()
                ? SmtpSslServer.create(server.getHost(), port)
                : SmtpServer.create(server.getHost(), port);
        smtpServer.authenticateWith(server.getUsername(), server.getPassword());
        return smtpServer;
    }

    private MailProperties.MailGroupProperties getGroup(String group) {
        Map<String, MailProperties.MailGroupProperties> groups = configuration.getGroups();
        if (groups == null || !groups.containsKey(group)) {
            LOG.MAIL.error("No group configuration found for {}, mail send operation will be aborted", group);
            return null;
        }
        return groups.get(group);
    }

    private Email buildEmail(String subject, String template, Map<String, Object> model, String group, List<String> ccs) {
        MailProperties.MailGroupProperties mailGroup = getGroup(group);
        if (mailGroup == null) return null;
        Email email = new Email();
        email.setSubject(Strings.isNullOrEmpty(subject) ? "Emily Email" : subject);
        email.setFrom(new MailAddress(mailGroup.getFrom()));
        if (mailGroup.getTo() != null && !mailGroup.getTo().isEmpty()) {
            mailGroup.getTo().stream().map(MailAddress::new).forEach(email::addTo);
        }
        List<String> combinedCcs = Lists.newArrayList();
        if (mailGroup.getCc() != null) {
            combinedCcs.addAll(mailGroup.getCc());
        }
        if (ccs != null) {
            combinedCcs.addAll(ccs);
        }
        combinedCcs.stream().map(MailAddress::new).forEach(email::addCc);
        String templateToUse = Strings.isNullOrEmpty(template) ? mailGroup.getTemplate() : template;
        email.addHtml(render.render(templateToUse, model));
        return email;
    }

    @Override
    public void send(String subject, Map<String, Object> model, String... groups) {
        send(subject, null, model, null, groups);
    }

    @Override
    public void send(String subject, Map<String, Object> model, List<String> ccs, String... groups) {
        send(subject, null, model, ccs, groups);
    }

    @Override
    public void send(String subject, String template, Map<String, Object> model, String... groups) {
        send(subject, template, model, null, groups);
    }

    @Retryable(label = "Email sending retry", maxAttempts = 10, backoff = @Backoff(delay = 3000, maxDelay = 60 * 1000, multiplier = 3))
    @Override
    public void send(String subject, String template, Map<String, Object> model, List<String> ccs, String... groups) {
        if (groups == null || groups.length == 0) {
            LOG.MAIL.warn("At least one mail group should be provided for {} {}", subject, template);
            return;
        }
        Email[] emails = Arrays.stream(groups)
                .map(g -> buildEmail(subject, template, model, g, ccs))
                .filter(Objects::nonNull)
                .toArray(Email[]::new);
        if (emails != null && emails.length > 0) {
            SendMailSession session = smtpServer.createSession();
            try {
                session.open();
                for (Email email : emails) {
                    session.sendMail(email);
                }
            } catch (Exception e) {
                LOG.MAIL.error("Cannot connect to smtp server!", e);
                throw Errors.rethrow(e);
            } finally {
                session.close();
            }
        }
    }
}
