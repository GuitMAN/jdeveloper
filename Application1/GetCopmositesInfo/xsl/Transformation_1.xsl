<?xml version="1.0" encoding="UTF-8" ?>
<?oracle-xsl-mapper
  <!-- SPECIFICATION OF MAP SOURCES AND TARGETS, DO NOT MODIFY. -->
  <mapSources>
    <source type="WSDL">
      <schema location="../GetCompositeInfo.wsdl"/>
      <rootElement name="Composites" namespace="http://xmlns.oracle.com/GetCompositesInfo/GetCompositesInfo/GetCompositeInfo"/>
    </source>
  </mapSources>
  <mapTargets>
    <target type="WSDL">
      <schema location="../GetCompositeInfo.wsdl"/>
      <rootElement name="processResponse" namespace="http://xmlns.oracle.com/GetCompositesInfo/GetCompositesInfo/GetCompositeInfo"/>
    </target>
  </mapTargets>
  <!-- GENERATED BY ORACLE XSL MAPPER 11.1.1.7.8(build 150622.2350.0222) AT [WED NOV 29 10:50:26 MSK 2017]. -->
?>
<xsl:stylesheet version="1.0"
                xmlns:bpws="http://schemas.xmlsoap.org/ws/2003/03/business-process/"
                xmlns:xp20="http://www.oracle.com/XSL/Transform/java/oracle.tip.pc.services.functions.Xpath20"
                xmlns:mhdr="http://www.oracle.com/XSL/Transform/java/oracle.tip.mediator.service.common.functions.MediatorExtnFunction"
                xmlns:bpel="http://docs.oasis-open.org/wsbpel/2.0/process/executable"
                xmlns:oraext="http://www.oracle.com/XSL/Transform/java/oracle.tip.pc.services.functions.ExtFunc"
                xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                xmlns:dvm="http://www.oracle.com/XSL/Transform/java/oracle.tip.dvm.LookupValue"
                xmlns:hwf="http://xmlns.oracle.com/bpel/workflow/xpath"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:med="http://schemas.oracle.com/mediator/xpath"
                xmlns:ids="http://xmlns.oracle.com/bpel/services/IdentityService/xpath"
                xmlns:bpm="http://xmlns.oracle.com/bpmn20/extensions"
                xmlns:xdk="http://schemas.oracle.com/bpel/extension/xpath/function/xdk"
                xmlns:xref="http://www.oracle.com/XSL/Transform/java/oracle.tip.xref.xpath.XRefXPathFunctions"
                xmlns:plnk="http://schemas.xmlsoap.org/ws/2003/05/partner-link/"
                xmlns:xsd="http://www.w3.org/2001/XMLSchema"
                xmlns:wsdl="http://schemas.xmlsoap.org/wsdl/"
                xmlns:ora="http://schemas.oracle.com/xpath/extension"
                xmlns:socket="http://www.oracle.com/XSL/Transform/java/oracle.tip.adapter.socket.ProtocolTranslator"
                xmlns:client="http://xmlns.oracle.com/GetCompositesInfo/GetCompositesInfo/GetCompositeInfo"
                xmlns:ldap="http://schemas.oracle.com/xpath/extension/ldap"
                exclude-result-prefixes="xsi xsl plnk xsd wsdl client bpws xp20 mhdr bpel oraext dvm hwf med ids bpm xdk xref ora socket ldap">
  <xsl:template match="/">
    <client:processResponse>
			<xsl:for-each select="/client:deployed-composites/composite-series/composite-revision">
				<client:Composite>
					<client:Name>
                                            <xsl:value-of select="../@name"/>
					</client:Name>
					 <client:State>
					  <xsl:value-of select="@state"/>
					</client:State>
					<client:Mode>
					  <xsl:value-of select="@mode"/>
					</client:Mode>
					<client:Revision>
                                            <xsl:value-of select="substring-after(@dn, '!')" />
					</client:Revision>    
					<client:Default>
                                            <xsl:value-of select="substring-after(../@default, '!')"/>
					</client:Default>
					<client:DeployedTime>
                                            <xsl:value-of select="composite/@deployedTime"/>
					</client:DeployedTime>  
				</client:Composite>
			</xsl:for-each>
    </client:processResponse>
  </xsl:template>
</xsl:stylesheet>
