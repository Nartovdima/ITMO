<#import "./common.ftlh" as c>

<@c.page>
    <div class="datatable">
        <div class="caption">Submissions</div>
        <table>
            <thead>
            <tr>
                <th>Id</th>
                <th>Handle</th>
                <th>Name</th>
            </tr>
            </thead>
            <tbody>
                <#list users as u>
                    <tr>
                        <td>${u.id}</td>
                        <td>${u.handle}</td>
                        <td>
                            <@c.userlink u false/></td>
                    </tr>
                </#list>
            </tbody>
        </table>
    </div>
</@c.page>
