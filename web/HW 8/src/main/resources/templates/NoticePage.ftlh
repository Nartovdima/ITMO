<#import "commons.ftlh" as c>

<@c.page>
    <div class="notice form-box">
        <div class="header">Create Notice</div>
        <div class="body">
            <form method="post" action="">
                <div class="field">
                    <div class="name">
                        <label for="text">Text</label>
                    </div>
                    <div class="value">
                        <textarea id="content" name="content"></textarea>
                    </div>
                    <@c.error "noticeForm.content"/>
                </div>
                <div class="button-field">
                    <input type="submit" value="Create notice">
                </div>
            </form>
        </div>
    </div>
</@c.page>