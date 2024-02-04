<#import "commons.ftlh" as c>

<@c.page>
    <div class="users datatable">
        <div class="caption">User</div>
        <table>
            <thead>
            <tr>
                <th>Id</th>
                <th>Login</th>
                <th>Created</th>
                <th>Status</th>
            </tr>
            </thead>
            <tbody>
            <#if users?size==0>
                <tr class="noData">
                    <td colspan="3">
                        No data
                    </td>
                </tr>
            <#else>
                <#list users as user>
                <tr>
                    <td class="id">${user.id}</td>
                    <td class="login">${user.login}</td>
                    <td class="creationTime">${user.creationTime}</td>
                    <td class="status">
                        <form method="post" action="">
                            <input type="hidden" name="id" value="${user.getId()}"/>
                            <input type="hidden" name="status" value="${user.isDisabled()?then('true' 'false')}"/>
                            <div class="button-field">
                                <#if user.isDisabled()>
                                    <input type="submit" value="Enable">
                                <#else>
                                    <input type="submit" value="Disable">
                                </#if>
                            </div>
                        </form>
                    </td>
                </tr>
                </#list>
            </#if>
            </tbody>
        </table>
    </div>
</@c.page>
