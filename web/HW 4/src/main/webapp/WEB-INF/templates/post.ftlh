<#-- @ftlvariable name="post_id" type="java.lang.Long" -->
<#import "./common.ftlh" as c>

<@c.page>
    <#assign postInd = -1/>
    <#if post_id?is_number>
        <#assign postInd = c.findBy(posts, "id", post_id)/>
    </#if>
    <#if postInd != -1>
        <@c.article posts[postInd] false/>
    <#else>
        No such post
    </#if>
</@c.page>