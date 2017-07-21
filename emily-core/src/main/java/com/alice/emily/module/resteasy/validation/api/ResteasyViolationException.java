package com.alice.emily.module.resteasy.validation.api;

import com.alice.emily.module.resteasy.validation.plugins.ConstraintTypeUtil11;
import com.alice.emily.module.resteasy.validation.plugins.GeneralValidatorImpl;
import com.alice.emily.module.resteasy.validation.plugins.SimpleViolationsContainer;
import org.jboss.resteasy.api.validation.ConstraintType.Type;
import org.jboss.resteasy.api.validation.ResteasyConstraintViolation;
import org.jboss.resteasy.plugins.providers.validation.ViolationsContainer;
import org.jboss.resteasy.spi.ResteasyConfiguration;
import org.jboss.resteasy.spi.ResteasyProviderFactory;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.ws.rs.core.MediaType;
import java.io.*;
import java.util.*;

/**
 * @author <a href="ron.sigal@jboss.com">Ron Sigal</a>
 * @version $Revision: 1.1 $
 *          <p>
 *          Copyright Mar 6, 2012
 * @TODO Need to work on representation of exceptions
 * @TODO Add javadoc.
 */
public class ResteasyViolationException extends ConstraintViolationException {
    private static final long serialVersionUID = 2623733139912277260L;

    private List<CloneableMediaType> accept;
    private Exception exception;

    private List<ResteasyConstraintViolation> fieldViolations;
    private List<ResteasyConstraintViolation> propertyViolations;
    private List<ResteasyConstraintViolation> classViolations;
    private List<ResteasyConstraintViolation> parameterViolations;
    private List<ResteasyConstraintViolation> returnValueViolations;

    private List<ResteasyConstraintViolation> allViolations;
    private List<List<ResteasyConstraintViolation>> violationLists;

    private transient ConstraintTypeUtil11 util = new ConstraintTypeUtil11();
    private boolean suppressPath;

    /**
     * New constructor
     *
     * @param constraintViolations
     */
    public ResteasyViolationException(Set<? extends ConstraintViolation<?>> constraintViolations) {
        super(constraintViolations);
        checkSuppressPath();
        accept = new ArrayList<>();
        accept.add(CloneableMediaType.TEXT_PLAIN_TYPE);
    }

    /**
     * New constructor
     *
     * @param constraintViolations
     * @param accept
     */
    public ResteasyViolationException(Set<? extends ConstraintViolation<?>> constraintViolations, List<MediaType> accept) {
        super(constraintViolations);
        checkSuppressPath();
        this.accept = toCloneableMediaTypeList(accept);
    }

    /**
     * New constructor
     *
     * @param container
     */
    public ResteasyViolationException(SimpleViolationsContainer container) {
        this(container.getViolations());
        exception = container.getException();
    }

    /**
     * New constructor
     *
     * @param container
     * @param accept
     */

    public ResteasyViolationException(SimpleViolationsContainer container, List<MediaType> accept) {
        this(container.getViolations(), accept);
        exception = container.getException();
    }

    public ResteasyViolationException(ViolationsContainer<?> container) {
        super(null);
        convertToStrings(container);
        exception = container.getException();
        accept = new ArrayList<>();
        accept.add(CloneableMediaType.TEXT_PLAIN_TYPE);
    }

    public ResteasyViolationException(ViolationsContainer<?> container, List<MediaType> accept) {
        super(null);
        convertToStrings(container);
        exception = container.getException();
        this.accept = toCloneableMediaTypeList(accept);
    }

    public ResteasyViolationException(String stringRep) {
        super(null);
        checkSuppressPath();
        convertFromString(stringRep);
    }

    public List<MediaType> getAccept() {
        return toMediaTypeList(accept);
    }

    public void setAccept(List<MediaType> accept) {
        this.accept = toCloneableMediaTypeList(accept);
    }

    public Exception getException() {
        return exception;
    }

    public void setException(Exception exception) {
        this.exception = exception;
    }

    public List<ResteasyConstraintViolation> getViolations() {
        convertViolations();
        if (allViolations == null) {
            allViolations = new ArrayList<>();
            allViolations.addAll(fieldViolations);
            allViolations.addAll(propertyViolations);
            allViolations.addAll(classViolations);
            allViolations.addAll(parameterViolations);
            allViolations.addAll(returnValueViolations);
        }
        return allViolations;
    }

    public List<ResteasyConstraintViolation> getFieldViolations() {
        convertViolations();
        return fieldViolations;
    }

    public List<ResteasyConstraintViolation> getPropertyViolations() {
        convertViolations();
        return propertyViolations;
    }

    public List<ResteasyConstraintViolation> getClassViolations() {
        convertViolations();
        return classViolations;
    }

    public List<ResteasyConstraintViolation> getParameterViolations() {
        convertViolations();
        return parameterViolations;
    }

    public List<ResteasyConstraintViolation> getReturnValueViolations() {
        convertViolations();
        return returnValueViolations;
    }

    public int size() {
        return getViolations().size();
    }

    public List<List<ResteasyConstraintViolation>> getViolationLists() {
        convertViolations();
        return violationLists;
    }

    public String toString() {
        convertViolations();
        StringBuilder sb = new StringBuilder();
        for (List<ResteasyConstraintViolation> violations : violationLists) {
            for (ResteasyConstraintViolation violation : violations) {
                sb.append(violation.toString()).append('\r');
            }
        }
        return sb.toString();
    }

    protected void convertToStrings(ViolationsContainer<?> container) {
        if (violationLists != null) {
            return;
        }
        violationLists = new ArrayList<>();
        fieldViolations = container.getFieldViolations();
        propertyViolations = container.getPropertyViolations();
        classViolations = container.getClassViolations();
        parameterViolations = container.getParameterViolations();
        returnValueViolations = container.getReturnValueViolations();

        violationLists.add(fieldViolations);
        violationLists.add(propertyViolations);
        violationLists.add(classViolations);
        violationLists.add(parameterViolations);
        violationLists.add(returnValueViolations);
    }

    protected void convertFromString(String stringRep) {
        convertViolations();
        InputStream is = new ByteArrayInputStream(stringRep.getBytes());
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String line;
        try {
            line = br.readLine();
            while (line != null) {
                Type type = Type.valueOf(line.substring(1, line.length() - 1));
                line = br.readLine();
                String path = line.substring(1, line.length() - 1);
                line = br.readLine();
                String message = line.substring(1, line.length() - 1);
                line = br.readLine();
                String value = line.substring(1, line.length() - 1);
                ResteasyConstraintViolation rcv = new ResteasyConstraintViolation(type, path, message, value);

                switch (type) {
                    case FIELD:
                        fieldViolations.add(rcv);
                        break;

                    case PROPERTY:
                        propertyViolations.add(rcv);
                        break;

                    case CLASS:
                        classViolations.add(rcv);
                        break;

                    case PARAMETER:
                        parameterViolations.add(rcv);
                        break;

                    case RETURN_VALUE:
                        returnValueViolations.add(rcv);
                        break;

                    default:
                        throw new RuntimeException("unexpected violation type: " + type);
                }
                line = br.readLine(); // consume ending '\r'
                line = br.readLine();
            }
        } catch (IOException e) {
            throw new RuntimeException("Unable to parse ResteasyViolationException");
        }

        violationLists = new ArrayList<>();
        violationLists.add(fieldViolations);
        violationLists.add(propertyViolations);
        violationLists.add(classViolations);
        violationLists.add(parameterViolations);
        violationLists.add(returnValueViolations);
    }

    protected int getField(int start, String line) {
        int beginning = line.indexOf('[', start);
        if (beginning == -1) {
            throw new RuntimeException("ResteasyViolationException has invalid format: " + line);
        }
        int index = beginning;
        int bracketCount = 1;
        while (++index < line.length()) {
            char c = line.charAt(index);
            if (c == '[') {
                bracketCount++;
            } else if (c == ']') {
                bracketCount--;
            }
            if (bracketCount == 0) {
                break;
            }
        }
        if (bracketCount != 0) {
            throw new RuntimeException("ResteasyViolationException has invalid format: " + line);
        }
        return index;
    }

    protected void checkSuppressPath() {
        ResteasyConfiguration context = ResteasyProviderFactory.getContextData(ResteasyConfiguration.class);
        if (context != null) {
            String s = context.getParameter(GeneralValidatorImpl.SUPPRESS_VIOLATION_PATH);
            if (s != null) {
                suppressPath = Boolean.parseBoolean(s);
            }
        }
    }

    protected void convertViolations() {
        if (violationLists != null) {
            return;
        }

        fieldViolations = new ArrayList<>();
        propertyViolations = new ArrayList<>();
        classViolations = new ArrayList<>();
        parameterViolations = new ArrayList<>();
        returnValueViolations = new ArrayList<>();

        if (getConstraintViolations() != null) {
            for (ConstraintViolation<?> constraintViolation : getConstraintViolations()) {
                ResteasyConstraintViolation rcv = convertViolation(constraintViolation);
                switch (rcv.getConstraintType()) {
                    case FIELD:
                        fieldViolations.add(rcv);
                        break;

                    case PROPERTY:
                        propertyViolations.add(rcv);
                        break;

                    case CLASS:
                        classViolations.add(rcv);
                        break;

                    case PARAMETER:
                        parameterViolations.add(rcv);
                        break;

                    case RETURN_VALUE:
                        returnValueViolations.add(rcv);
                        break;

                    default:
                        throw new RuntimeException("unexpected violation type: " + rcv.getConstraintType());
                }
            }
        }

        violationLists = new ArrayList<>();
        violationLists.add(fieldViolations);
        violationLists.add(propertyViolations);
        violationLists.add(classViolations);
        violationLists.add(parameterViolations);
        violationLists.add(returnValueViolations);
    }

    protected ResteasyConstraintViolation convertViolation(ConstraintViolation<?> violation) {
        Type ct = util.getConstraintType(violation);
        String path = (suppressPath ? "*" : violation.getPropertyPath().toString());
        return new ResteasyConstraintViolation(ct, path, violation.getMessage(), convertArrayToString(violation.getInvalidValue()));
    }

    protected static String convertArrayToString(Object o) {
        String result;
        if (o instanceof Object[]) {
            Object[] array = Object[].class.cast(o);
            StringBuilder sb = new StringBuilder("[").append(convertArrayToString(array[0]));
            for (int i = 1; i < array.length; i++) {
                sb.append(", ").append(convertArrayToString(array[i]));
            }
            sb.append("]");
            result = sb.toString();
        } else {
            result = (o == null ? "" : o.toString());
        }
        return result;
    }

    /**
     * It seems that EJB3 wants to clone ResteasyViolationException,
     * and MediaType is not serializable.
     */
    static class CloneableMediaType implements Serializable {
        public static final CloneableMediaType TEXT_PLAIN_TYPE = new CloneableMediaType("plain", "text");
        private static final long serialVersionUID = 9179565449557464429L;
        private String type;
        private String subtype;
        private Map<String, String> parameters;

        CloneableMediaType(MediaType mediaType) {
            type = mediaType.getType();
            subtype = mediaType.getSubtype();
            parameters = new HashMap<>(mediaType.getParameters());
        }

        CloneableMediaType(String type, String subtype) {
            this.type = type;
            this.subtype = subtype;
        }

        public MediaType toMediaType() {
            return new MediaType(type, subtype, parameters);
        }
    }

    protected static List<CloneableMediaType> toCloneableMediaTypeList(List<MediaType> list) {
        List<CloneableMediaType> cloneableList = new ArrayList<CloneableMediaType>();
        for (MediaType aList : list) {
            cloneableList.add(new CloneableMediaType(aList));
        }
        return cloneableList;
    }

    protected static List<MediaType> toMediaTypeList(List<CloneableMediaType> cloneableList) {
        List<MediaType> list = new ArrayList<MediaType>();
        for (CloneableMediaType cmt : cloneableList) {
            list.add(new MediaType(cmt.type, cmt.subtype, cmt.parameters));
        }
        return list;
    }
}
