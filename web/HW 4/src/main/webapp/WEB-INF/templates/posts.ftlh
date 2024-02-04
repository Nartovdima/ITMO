<#-- @ftlvariable name="user_id" type="java.lang.Long" -->
<#import "./common.ftlh" as c>

<@c.page>
    <#assign userPosts = []/>
    <#if user_id?is_number>
        <#assign userPosts = posts?filter(p -> p.user_id == user_id)/>
    </#if>
    <#if userPosts?size != 0>
        <#list userPosts as p>
            <@c.article p true/>
        </#list>
    <#else>
        This user doesn't have any posts(
    </#if>
</@c.page>