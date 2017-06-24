
<%@ page import="fotostore.LimitedAccess" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'limitedAccess.label', default: 'LimitedAccess')}" />
		<title><g:message code="default.show.label" args="[entityName]" /></title>
	</head>
	<body>
		<a href="#show-limitedAccess" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
		<div class="nav" role="navigation">
			<ul>
				<li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
				<li><g:link class="list" action="index"><g:message code="default.list.label" args="[entityName]" /></g:link></li>
				<li><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></li>
			</ul>
		</div>
		<div id="show-limitedAccess" class="content scaffold-show" role="main">
			<h1><g:message code="default.show.label" args="[entityName]" /></h1>
			<g:if test="${flash.message}">
			<div class="message" role="status">${flash.message}</div>
			</g:if>
			<ol class="property-list limitedAccess">
			
				<g:if test="${limitedAccessInstance?.key}">
				<li class="fieldcontain">
					<span id="key-label" class="property-label"><g:message code="limitedAccess.key.label" default="Key" /></span>
					
						<span class="property-value" aria-labelledby="key-label"><g:fieldValue bean="${limitedAccessInstance}" field="key"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${limitedAccessInstance?.foto}">
				<li class="fieldcontain">
					<span id="foto-label" class="property-label"><g:message code="limitedAccess.foto.label" default="Foto" /></span>
					
						<span class="property-value" aria-labelledby="foto-label"><g:link controller="foto" action="show" id="${limitedAccessInstance?.foto?.id}">${limitedAccessInstance?.foto?.encodeAsHTML()}</g:link></span>
					
				</li>
				</g:if>
			
				<g:if test="${limitedAccessInstance?.start}">
				<li class="fieldcontain">
					<span id="start-label" class="property-label"><g:message code="limitedAccess.start.label" default="Start" /></span>
					
						<span class="property-value" aria-labelledby="start-label"><g:formatDate date="${limitedAccessInstance?.start}" /></span>
					
				</li>
				</g:if>
			
				<g:if test="${limitedAccessInstance?.key}">
				<li class="fieldcontain">
					<span id="url-label" class="property-label"><g:message code="limitedAccess.url.label" default="Limimted access URL" /></span>
					
						<span class="property-value" aria-labelledby="url-label">
							<g:link controller="limitedAccess" action="image" params="[key: limitedAccessInstance.key]">
								${createLink(action: 'image', controller: 'limitedAccess', params: [key: limitedAccessInstance.key], absolute: true)}
							</g:link>
						</span>
					
				</li>
				</g:if>
			
			</ol>
			<g:form url="[resource:limitedAccessInstance, action:'delete']" method="DELETE">
				<fieldset class="buttons">
					<g:link class="edit" action="edit" resource="${limitedAccessInstance}"><g:message code="default.button.edit.label" default="Edit" /></g:link>
					<g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
				</fieldset>
			</g:form>
		</div>
	</body>
</html>
