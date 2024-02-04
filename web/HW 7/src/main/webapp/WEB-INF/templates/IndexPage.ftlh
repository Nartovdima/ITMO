<#import "commons.ftlh" as c>

<@c.page>
    <div class="articles">
        <div class="caption">Articles</div>
        <div class="noData">No data</div>
        <ul>
            <template>
                <li class="article">
                    <article>
                        <div class="title"></div>
                        <div class="information"></div>
                        <div class="body">
                            <p></p>
                        </div>
                        <div class="footer">
                            <div class="left">
                                <img src="img/voteup.png" title="Vote Up" alt="Vote Up"/>
                                <span class="positive-score">+173</span>
                                <img src="img/votedown.png" title="Vote Down" alt="Vote Down"/>
                            </div>
                            <div class="right">
                                <img src="img/date_16x16.png" title="Publish Time" alt="Publish Time"/>
                                2 days ago
                                <img src="img/comments_16x16.png" title="Comments" alt="Comments"/>
                                <a href="#">68</a>
                            </div>
                        </div>
                    </article>
                </li>
            </template>
        </ul>
    </div>

    <script>
        $(function () {
            const $noData = $(".articles .noData");
            $noData.text("Loading...");

            setup(
                {
                    action: "findAll"
                },
                function(response) {
                    response["articles"].forEach(function(article) {
                        $noData.hide();
                        const $li = $($(".articles template").prop("content")).clone();

                        $li.find(".article .title").text(article["title"]);
                        $li.find(".article .body p").text(article["text"]);
                        $li.find(".article .information").text(response["userById"][article["userId"]]);

                        $(".articles ul").append($li);
                    });
                }
            );
        })
    </script>
</@c.page>
