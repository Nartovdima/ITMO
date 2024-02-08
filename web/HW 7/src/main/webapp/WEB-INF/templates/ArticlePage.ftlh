<#-- @ftlvariable name="message" type="ru.itmo.wp.model.domain.Talk" -->
<#-- @ftlvariable name="users" type="ru.itmo.wp.model.domain.User[]" -->
<#-- @ftlvariable name="user" type="ru.itmo.wp.model.domain.User" -->
<#-- @ftlvariable name="userById" type="java.util.HashMap<ru.itmo.wp.model.domain.User, java.lang.String>" -->
<#-- @ftlvariable name="error" type="java.lang.String" -->

<#import "commons.ftlh" as c>

<@c.page>
    <div class="article form-box">
        <form method="post" action="">
            <input type="hidden" name="action" value="makeArticle"/>
            <div class="field">
                <div class="name">
                    <label for="title">Title</label>
                </div>
                <div class="value">
                    <input id="title" name="title"/>
                </div>
            </div>
            <div class="field">
                <div class="name">
                    <label for="text">Message</label>
                </div>
                <div class="value">
                    <textarea id="text" name="text"></textarea>
                </div>

            </div>
            <div class="button-field">
                <input type="submit" value="Done">
            </div>
            <div class="error"></div>
        </form>
    </div>

    <script>
        $(function () {
            $(".article form").submit(function () {
                const title = $(this).find("input[name='title']").val();
                const text = $(this).find("textarea[name='text']").val();
                const $error = $(this).find(".error");

                setup({
                    action: "makeArticle",
                    title,
                    text
                },function (response) {}, $error);


                return false;
            });
        })
    </script>
</@c.page>