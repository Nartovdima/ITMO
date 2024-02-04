<#import "./common.ftlh" as c>

<@c.page>
    <#list posts?reverse as p>
        <@c.article p true/>
    </#list>
</@c.page>

