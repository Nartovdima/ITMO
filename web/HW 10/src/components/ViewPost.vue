<template>
    <div class="article">
        <article>
            <div class="title">
                <a href="#" @click.prevent="gotoPost(post.id)">{{ post.title }}</a>
            </div>
            <div class="information">{{ post.author }}</div>
            <div class="body">
                <p v-for="(para, index) in post.text.split('\n')" :key="index">
                    {{ para }}
                </p>
            </div>
            <div class="footer">
                <div class="left">
                    <img src="../assets/img/voteup.png" title="Vote Up" alt="Vote Up"/>
                    <span class="positive-score">+173</span>
                    <img src="../assets/img/votedown.png" title="Vote Down" alt="Vote Down"/>
                </div>
                <div class="right">
                    <img src="../assets/img/comments_16x16.png" title="Comments" alt="Comments"/>
                    <a>{{ post.comments.length }}</a>
                </div>
            </div>
        </article>
        <template v-if="showComments">
            <div class="comments">
                <div class="comment" v-for="(comment, index) in comments" :key="index">
                    <div class="comment-author">
                        {{ comment.author }}
                    </div>
                    <div class="content">
                        <p v-for="(para, index2) in comment.text.split('\n')" :key="index2">
                            {{ para }}
                        </p>
                    </div>
                </div>
            </div>
        </template>
    </div>
</template>

<script>
export default {
    name: "ViewPost",
    props: ["post", "showComments", "comments"],
    methods: {
        gotoPost: function (postId) {
            this.$root.$emit("onGotoPost", postId);
        }
    }
}
</script>

<style scoped>
    .comment {
        border: 1px solid black;
        border-radius: 8px;
        margin-bottom: 1rem;
        padding-left: 0.25rem;
    }
    .comment .content p {
        margin-top: 0.1rem;
        margin-bottom: 0.1rem;
    }

    .comment .comment-author {
        margin-bottom: 0.25rem;

    }
</style>