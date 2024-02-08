<#-- @ftlvariable name="handle" type="java.lang.String" -->
<#-- @ftlvariable name="user_id" type="java.lang.Long" -->
<#import "./common.ftlh" as c>


<@c.page>
    <div class="profile">
        <#assign uId = -1/>
        <#if handle?? && handle?is_string>
            <#assign uId = c.findBy(users, "handle", handle)!/>
        <#elseif user_id?? && user_id?is_number>
            <#assign uId = c.findBy(users, "id", user_id)!/>
        </#if>

        <#if uId != -1>
            <#assign u = users[uId]/>
        </#if>
        <#if u?has_content>
            <div class="profileInfo">
                <div class="title">
                    <@c.userlink u false/>
                </div>
                <ul>
                    <li>id: ${u.id}</li>
                    <li>handle: ${u.handle}</li>
                    <li>name: ${u.name}</li>
                    <li>posts: <@c.postslink u/></li>
                </ul>
            </div>
            <div class="avatar">
                <img alt="human head" src="/img/no-title.png">
            </div>
            </div>
            <ul class="navigation">
                <li class="leftArrow">
                    <#if users[uId - 1]??>
                        <a href="/user?handle=${users[uId - 1].handle}">←</a>
                    <#else>
                        ←
                    </#if>
                </li>
                <li class="rightArrow">
                    <#if users[uId + 1]??>
                        <a href="/user?handle=${users[uId + 1].handle}">→</a>
                    <#else>
                        →
                    </#if>
                </li>
            </ul>
        <#else>
            No such user
        </#if>
</@c.page>

