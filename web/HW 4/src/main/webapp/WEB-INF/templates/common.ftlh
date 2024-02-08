<#-- @ftlvariable name="currentPage" type="java.lang.String" -->
<#macro page>
    <!DOCTYPE html>
    <html lang="ru">
    <head>
        <meta charset="UTF-8">
        <title>Codeforces</title>
        <link rel="stylesheet" href="/css/normalize.css">
        <link rel="stylesheet" href="/css/style.css">
        <link rel="icon" href="/favicon.ico">
    </head>
    <body>
    <@header/>
    <div class="middle">
        <@sidebar/>
        <main>
            <#nested/>
        </main>
    </div>
    <@footer/>
    </body>
    </html>
</#macro>

<#macro header>
    <header>
        <a href="#" class="logo"><img alt="Codeforces" title="Codeforces"
                                      src="/img/codeforces-logo-with-telegram.png"></a>
        <div class="languages">
            <a href="#"><img alt="In English" title="In English"
                             src="/img/gb.png"></a>
            <a href="#"><img alt="In Russian" title="In Russian"
                             src="/img/ru.png"></a>
        </div>
        <div class="enter-or-register-box">
            <#if user??>
                <@userlink user true/>
                |
                <a href="/logout">Logout</a>
            <#else>
                <a href="/enter">Enter</a>
                |
                <a href="/register">Register</a>
            </#if>
        </div>
        <nav>
            <#assign items=[
            {"href": "/index", "name": "Home"},
            {"href": "/misc/help", "name": "Help"},
            {"href": "/contests", "name": "Contests"},
            {"href": "/users", "name": "Users"}
            ]/>
            <ul>
                <#list items as item>
                    <li>
                        <@navLinkUnderliner item.href item.name currentPage/>
                    </li>
                </#list>
            </ul>
        </nav>
    </header>
</#macro>

<#macro sidebar>
    <aside>
        <#list posts?reverse as p>

            <section>
                <div class="header">Post ${p.id}</div>
                <div class="body">
                    ${p.text?truncate(350, "...")}
                </div>
                <div class="footer">
                    <@postlink p.id/>
                </div>
            </section>
        </#list>
    </aside>
</#macro>

<#macro footer>
    <footer>
        2010-2021 &copy; by Mike Mirzayanov
    </footer>
</#macro>

<#macro userlink user nameOnly>
<#-- @ftlvariable name="user" type="ru.itmo.web.hw4.model.User" -->
    <#if nameOnly>
        <a href="/user?handle=${user.handle}">${user.name}</a>
    <#else>
        <div class="userLink">
            <a href="/user?handle=${user.handle}" style="color: ${user.color}; text-decoration: none">${user.name}</a>
        </div>
    </#if>
</#macro>

<#macro postslink user>
    <a href="/posts?user_id=${user.id}">${posts?filter(p -> p.user_id == user.id)?size}</a>
</#macro>

<#macro postlink postId>
    <a href="/post?post_id=${postId}">View all</a>
</#macro>

<#function findBy items key value>
    <#assign result = -1/>
    <#list items as item>
        <#if item[key] == value>
            <#assign result = item?index/>
        </#if>
    </#list>
    <#return result/>
</#function>

<#macro navLinkUnderliner option name origin>
    <#if option == origin>
        <a href=${option} class="underlined">${name}</a>
    <#else>
        <a href=${option}>${name}</a>
    </#if>
</#macro>

<#macro article post isTrunc>
    <article>
        <div class="title">${post.title}</div>
        <div class="information">By ${users[c.findBy(users, "id", post.user_id)].name}</div>
        <div class="body">
            <p>
                <#if isTrunc>
                    ${post.text?truncate(350, "...")}
                <#else>
                    ${post.text}
                </#if>
            </p>
        </div>
        <div class="footer">
            <div class="left">
                <img src="img/voteup.png" title="Vote Up" alt="Vote Up"/>
                <span class="positive-score">0</span>
                <img src="img/votedown.png" title="Vote Down" alt="Vote Down"/>
            </div>
        </div>
    </article>
</#macro>

