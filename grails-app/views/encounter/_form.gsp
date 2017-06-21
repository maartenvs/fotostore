<%@ page import="fotostore.Encounter" %>



<div class="fieldcontain ${hasErrors(bean: encounterInstance, field: 'name', 'error')} required">
	<label for="name">
		<g:message code="encounter.name.label" default="Name" />
		<span class="required-indicator">*</span>
	</label>
	<g:textField name="name" required="" value="${encounterInstance?.name}"/>

</div>

<div class="fieldcontain ${hasErrors(bean: encounterInstance, field: 'date', 'error')} required">
	<label for="date">
		<g:message code="encounter.date.label" default="Date" />
		<span class="required-indicator">*</span>
	</label>
	<g:datePicker name="date" precision="day"  value="${encounterInstance?.date}"  />

</div>

<div class="fieldcontain ${hasErrors(bean: encounterInstance, field: 'fotos', 'error')} ">
	<label for="fotos">
		<g:message code="encounter.fotos.label" default="Fotos" />
		
	</label>
	<g:select name="fotos" from="${fotostore.Foto.list()}" multiple="multiple" optionKey="id" size="5" value="${encounterInstance?.fotos*.id}" class="many-to-many"/>

</div>

