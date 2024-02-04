<#-- @ftlvariable name="user" type="ru.itmo.wp.domain.User" -->
<#import "commons.ftlh" as c>

<@c.page>
    <div class="User Page">
        <#if user?has_content>
            <ul>
                <li>Id: ${user.getId()}</li>
                <li>Login: ${user.getLogin()}</li>
                <li>Creation time: ${user.getCreationTime()}</li>
            </ul>
        <#else>
            No such user(
        </#if>
    </div>
</@c.page>
