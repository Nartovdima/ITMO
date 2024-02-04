<#-- @ftlvariable name="errorCode" type="java.lang.Long" -->
<#-- @ftlvariable name="errorMessage" type="java.lang.String" -->
<#import "./common.ftlh" as c>

<@c.page>
    <div class="errorCode">
        ${errorCode}
    </div>
    <div class="errorMessage">
        ${errorMessage}
    </div>
</@c.page>