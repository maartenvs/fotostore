<%@ page import="fotostore.LimitedAccess" %>



<div class="fieldcontain ${hasErrors(bean: limitedAccessInstance, field: 'key', 'error')} required">
	<label for="key">
		<g:message code="limitedAccess.key.label" default="Key" />
		<span class="required-indicator">*</span>
	</label>
	<g:textField name="key" required="" value="${limitedAccessInstance?.key}"/>

</div>

<div class="fieldcontain ${hasErrors(bean: limitedAccessInstance, field: 'foto', 'error')} required">
	<label for="foto">
		<g:message code="limitedAccess.foto.label" default="Foto" />
		<span class="required-indicator">*</span>
	</label>
	<g:select id="foto" name="foto.id" from="${fotostore.Foto.list()}" optionKey="id" required="" value="${limitedAccessInstance?.foto?.id}" class="many-to-one"/>

</div>

<div class="fieldcontain ${hasErrors(bean: limitedAccessInstance, field: 'start', 'error')} required">
	<label for="start">
		<g:message code="limitedAccess.start.label" default="Start" />
		<span class="required-indicator">*</span>
	</label>
	<g:datePicker name="start" precision="day"  value="${limitedAccessInstance?.start}"  />

</div>

