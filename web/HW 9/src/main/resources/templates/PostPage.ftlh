<#-- @ftlvariable name="post" type="ru.itmo.wp.domain.Post" -->
<#import "commons.ftlh" as c>

<@c.page>
    <#if post?has_content>
        <@c.post post=post/>

        <#if user?has_content>
            <div class="comment form-box">
                <div class="header">Create Comment</div>
                <div class="body">
                    <form method="post" action="">
                        <div class="field">
                            <div class="name">
                                <label for="text">Text</label>
                            </div>
                            <div class="value">
                                <textarea id="text" name="text">${commentForm.getText()!}</textarea>
                            </div>
                            <@c.error "commentForm.text"/>
                        </div>
                        <div class="button-field">
                            <input type="submit" value="Create comment">
                        </div>
                    </form>
                </div>
            </div>
        </#if>
        <@c.post_comments post=post/>
    <#else>
        No such post(
    </#if>

</@c.page>
