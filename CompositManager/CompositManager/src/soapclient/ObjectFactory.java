
package soapclient;

import javax.xml.bind.annotation.XmlRegistry;


/**
 * This object contains factory methods for each
 * Java content interface and Java element interface
 * generated in the soapclient package.
 * <p>An ObjectFactory allows you to programatically
 * construct new instances of the Java representation
 * for XML content. The Java representation of XML
 * content can consist of schema derived interfaces
 * and classes representing the binding of schema
 * type definitions, element declarations and model
 * groups.  Factory methods for each of these are
 * provided in this class.
 *
 */
@XmlRegistry
public class ObjectFactory {


    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: soapclient
     *
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link ProcessResponse.Composite }
     *
     */
    public ProcessResponse.Composite createProcessResponseComposite() {
        return new ProcessResponse.Composite();
    }

    /**
     * Create an instance of {@link DeployedComposites.CompositeSeries.CompositeRevision.Composite }
     *
     */
    public DeployedComposites.CompositeSeries.CompositeRevision.Composite createDeployedCompositesCompositeSeriesCompositeRevisionComposite() {
        return new DeployedComposites.CompositeSeries.CompositeRevision.Composite();
    }

    /**
     * Create an instance of {@link DeployedComposites.CompositeSeries }
     *
     */
    public DeployedComposites.CompositeSeries createDeployedCompositesCompositeSeries() {
        return new DeployedComposites.CompositeSeries();
    }

    /**
     * Create an instance of {@link DeployedComposites.CompositeSeries.CompositeRevision }
     *
     */
    public DeployedComposites.CompositeSeries.CompositeRevision createDeployedCompositesCompositeSeriesCompositeRevision() {
        return new DeployedComposites.CompositeSeries.CompositeRevision();
    }

    /**
     * Create an instance of {@link Process }
     *
     */
    public Process createProcess() {
        return new Process();
    }

    /**
     * Create an instance of {@link ProcessResponse }
     *
     */
    public ProcessResponse createProcessResponse() {
        return new ProcessResponse();
    }

    /**
     * Create an instance of {@link DeployedComposites }
     *
     */
    public DeployedComposites createDeployedComposites() {
        return new DeployedComposites();
    }

}
