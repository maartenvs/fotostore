<%@ page import="fotostore.Foto" %>



<div class="fieldcontain ${hasErrors(bean: fotoInstance, field: 'type', 'error')} required">
	<label for="type">
		<g:message code="foto.type.label" default="Type" />
		<span class="required-indicator">*</span>
	</label>
	<g:textField name="type" required="" value="${fotoInstance?.type}"/>

</div>

<div class="fieldcontain ${hasErrors(bean: fotoInstance, field: 'image', 'error')} required">
	<label for="image">
		<g:message code="foto.image.label" default="Image" />
		<span class="required-indicator">*</span>
	</label>
	<input type="file" id="image" name="image" />

</div>

