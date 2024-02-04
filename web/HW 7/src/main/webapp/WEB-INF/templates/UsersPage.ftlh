<#-- @ftlvariable name="users" type="ru.itmo.wp.model.domain.User[]" -->
<#-- @ftlvariable name="user" type="ru.itmo.wp.model.domain.User" -->
<#import "commons.ftlh" as c>

<@c.page>
    <div class="users datatable">
        <div class="caption">User</div>
        <table>
            <thead>
            <tr>
                <th>Id</th>
                <th>Login</th>
                <#if user?? && user.isAdminStatus()>
                    <th>Admin Status</th>
                </#if>
            </tr>
            </thead>
            <tbody>
            <template>
                <tr>
                    <td class="user-id"></td>
                    <td class="user_login"></td>
                    <#if user?? && user.isAdminStatus()>
                        <td class="adminStatus">
                            <div class="setStatus">
                                <input name="changeStatus" type="button">
                            </div>
                        </td>
                    </#if>
                </tr>
            </template>
            <tr class="noData">
                <#if user?? && user.isAdminStatus()>
                    <td colspan="3">
                        No data
                    </td>
                <#else>
                    <td colspan="2">
                        No data
                    </td>
                </#if>
            </tr>
            </tbody>
        </table>
    </div>

    <script>
        $(function () {
            const $noData = $(".users .noData");
            $noData.find("td").text("Loading...");

            setup(
                {
                    action: "findAll"
                },
                function(response) {
                    response["users"].forEach(function(user) {
                        $noData.hide();
                        const $tr = $($(".users template").prop("content")).clone();
                        $tr.find(".user-id").text(user["id"]);
                        $tr.find(".user_login").text(user["login"]);
                        $tr.find("tr").attr("data-userId", user["id"]);
                        $tr.find("input[name='changeStatus']").attr("id", user["id"]);
                        if (user["adminStatus"]) {
                            $tr.find("input[name='changeStatus']").attr("value", "disable");
                        } else {
                            $tr.find("input[name='changeStatus']").attr("value", "enable");
                        }
                        $(".users tbody").append($tr);
                    });
                    $(".users tr").click(function() {
                        setup(
                            {
                                action: "findUser",
                                userId: $(this).attr("data-userId")
                            },
                            function (response) {
                                notify(response["user"].creationTime);
                            }
                        );
                    });
                    $(".users input[name='changeStatus']").click(function() {
                        const btn = $(this);
                        setup(
                            {
                                action: "changeAdminStatus",
                                operatedUserId: btn.attr("id"),
                                status: btn.val()
                            },
                            function (response) {
                                if (response["success"]) {
                                    if (btn.val() === "enable") {
                                        btn.val("disable");
                                    } else {
                                        btn.val("enable");
                                    }
                                }
                            }
                        );
                    });
                }
            );
        });
    </script>
</@c.page>
