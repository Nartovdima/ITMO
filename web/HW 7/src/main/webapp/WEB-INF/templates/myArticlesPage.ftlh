<#-- @ftlvariable name="articles" type="ru.itmo.wp.model.domain.Article[]" -->
<#import "commons.ftlh" as c>

<@c.page>
    <div class="my-articles datatable">
        <div class="caption">Articles</div>
        <table>
            <thead>
            <tr>
                <th>Id</th>
                <th>Title</th>
                <th>Hide/Show</th>
            </tr>
            </thead>
            <tbody>
            <#if articles?has_content>
                <#list articles as article>
                    <tr>
                        <td>${article.getId()}</td>
                        <td>${article.getTitle()}</td>
                        <td>
                            <#if article.isHidden()>
                                <input name="changeVisibility" id="${article.getId()}" type="button" value="show"/>
                            <#else>
                                <input name="changeVisibility" id="${article.getId()}" type="button" value="hide"/>
                            </#if>
                        </td>
                    </tr>
                </#list>
            <#else>
                <tr class="noData">
                    <td colspan="3">
                        No data
                    </td>
                </tr>
            </#if>
            </tbody>
        </table>
        <div class="error"></div>
    </div>
    <script>
        $(function() {

            $(".my-articles tr input[name='changeVisibility']").click(function(){
                const buttonElem = $(this);
                const $error = $(".my-articles").find(".error");
                setup(
                    {
                        action: "changeVisibility",
                        articleId: buttonElem.attr("id"),
                        visibility: buttonElem.val()
                    },
                    function(response) {
                        if (response["success"] === true) {
                            if (buttonElem.val() === "hide") {
                                buttonElem.val("show");
                            } else {
                                buttonElem.val("hide");
                            }
                        }
                    }, $error
                );
            });
        })
    </script>
</@c.page>
