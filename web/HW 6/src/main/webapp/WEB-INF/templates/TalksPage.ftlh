<#-- @ftlvariable name="message" type="ru.itmo.wp.model.domain.Talk" -->
<#-- @ftlvariable name="users" type="ru.itmo.wp.model.domain.User[]" -->
<#-- @ftlvariable name="user" type="ru.itmo.wp.model.domain.User" -->
<#-- @ftlvariable name="userById" type="java.util.HashMap<ru.itmo.wp.model.domain.User, java.lang.String>" -->
<#-- @ftlvariable name="error" type="java.lang.String" -->

<#import "commons.ftlh" as c>

<@c.page>
    <div class="message-box">
        <div class="name">
            Target User
        </div>
        <form method="post" action="">
            <input type="hidden" name="action" value="sendMessage"/>
            <select class="value" name="targetUser">
                <#list users as currUser>
                    <#if currUser.getId() != user.getId()>
                        <option value="${currUser.getId()}">${currUser.getLogin()}</option>
                    </#if>
                </#list>
            </select>
            <div class="field">
                <div class="name">
                    <label for="text">Message</label>
                </div>
                <div class="value">
                    <textarea name="text">${text!}</textarea>
                </div>
                <#if error??>
                    <div class="error">
                        ${error!}
                    </div>
                </#if>
                <div class="button-field">
                    <input type="submit" value="Send">
                </div>
            </div>
        </form>
    </div>
    <div class="talk">
        <div class="caption">Talk</div>
        <table>
            <thead>
            <tr>
                <th>sourceUser</th>
                <th>targetUser</th>
                <th>text</th>
                <th>creationTime</th>
            </tr>
            </thead>
            <tbody>
            <#list talk as message>
                <tr>
                    <td>${userById[message.getSourceUserId()?string]}</td>
                    <td>${userById[message.getTargetUserId()?string]}</td>
                    <td>${message.getText()}</td>
                    <td>${message.getCreationTime()}</td>
                </tr>
            </#list>
            </tbody>
        </table>
    </div>
</@c.page>