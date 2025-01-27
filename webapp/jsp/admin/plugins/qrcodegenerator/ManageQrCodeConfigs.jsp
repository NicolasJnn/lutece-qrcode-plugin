<jsp:useBean id="manageqrcodeconfigQrCodeConfig" scope="session" class="fr.paris.lutece.plugins.qrcodegenerator.web.QrCodeConfigJspBean" />
<% String strContent = manageqrcodeconfigQrCodeConfig.processController ( request , response ); %>

<%@ page errorPage="../../ErrorPage.jsp" %>
<jsp:include page="../../AdminHeader.jsp" />

<%= strContent %>

<%@ include file="../../AdminFooter.jsp" %>
