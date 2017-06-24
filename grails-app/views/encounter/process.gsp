
<%@ page import="fotostore.Encounter" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'encounter.label', default: 'Encounter')}" />
		<title><g:message code="default.show.label" args="[entityName]" /></title>
	</head>
	<body>
		<a href="#show-encounter" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
		<div class="nav" role="navigation">
			<ul>
				<li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
				<li><g:link class="list" action="index"><g:message code="default.list.label" args="[entityName]" /></g:link></li>
				<li><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></li>
			</ul>
		</div>
		<div id="show-encounter" class="content scaffold-show" role="main">
			<h1><g:message code="default.process.label" args="[entityName]" /></h1>
			<g:if test="${flash.message}">
			<div class="message" role="status">${flash.message}</div>
			</g:if>
			<ol class="property-list encounter">
			
				<g:if test="${encounterInstance?.name}">
				<li class="fieldcontain">
					<span id="name-label" class="property-label"><g:message code="encounter.name.label" default="Name" /></span>
					
						<span class="property-value" aria-labelledby="name-label"><g:fieldValue bean="${encounterInstance}" field="name"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${encounterInstance?.date}">
				<li class="fieldcontain">
					<span id="date-label" class="property-label"><g:message code="encounter.date.label" default="Date" /></span>
					
						<span class="property-value" aria-labelledby="date-label"><g:formatDate date="${encounterInstance?.date}" /></span>
					
				</li>
				</g:if>
			
				<g:if test="${encounterInstance?.fotos}">
				<li class="fieldcontain">
					<span id="fotos-label" class="property-label"><g:message code="encounter.submittedfotos.label" default="Submitted links for fotos" /></span>
					
						<g:each in="${processedFotos}" var="processedFoto">
							<span class="property-value" aria-labelledby="fotos-label">
								<p>Type: <g:fieldValue bean="${processedFoto.foto}" field="type"/></p>
								<p>Key: ${processedFoto.temporaryAccess.key}</p>
								<p>URL:
									<g:link controller="limitedAccess" action="image" params="[key: processedFoto.temporaryAccess.key]">
										${createLink(action: 'image', controller: 'limitedAccess', params: [key: processedFoto.temporaryAccess.key], absolute: true)}
									</g:link>
								</p>
								<hr/>
							</span>
						</g:each>
					
				</li>
				</g:if>
			
				<li class="fieldcontain">
					<span id="httpResult-label" class="property-label"><g:message code="encounter.process.httpResult.label" default="HTTP result" /></span>
					
						<span class="property-value" aria-labelledby="httpResult-label">${httpResult}</span>
					
				</li>

				<li class="fieldcontain">
					<span id="jobId-label" class="property-label"><g:message code="encounter.process.jobId.label" default="Job ID" /></span>
					
						<span class="property-value" aria-labelledby="jobId-label">${jobId}</span>
					
				</li>

			</ol>
			<fieldset class="buttons">
				<g:link class="save" action="process" resource="${encounterInstance}"><g:message code="default.button.process.label" default="Process" /></g:link>
			</fieldset>
		</div>
	</body>
</html>
