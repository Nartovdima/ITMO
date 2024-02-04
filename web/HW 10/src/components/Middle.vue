<template>
    <div class="middle">
        <Sidebar :posts="viewPosts.slice(0, 2)"/>
        <main>
            <Index v-if="page === 'Index'" :posts="viewPosts" />
            <Enter v-if="page === 'Enter'"/>
            <Register v-if="page === 'Register'"/>
            <UsersList v-if="page === 'UsersList'" :users="viewUsers"/>
            <WritePost v-if="page === 'WritePost'"/>
            <EditPost v-if="page === 'EditPost'"/>
            <Post v-if="page === 'Post'" :post="viewPost" :comments="comments"/>
        </main>
    </div>
</template>

<script>
import Sidebar from "./sidebar/Sidebar";
import Index from "./page/Index";
import Enter from "./page/Enter";
import UsersList from "./page/UsersList";
import Register from "./page/Register";
import WritePost from "./page/WritePost";
import EditPost from "./page/EditPost";
import Post from "@/components/page/Post.vue";



export default {
    name: "Middle",
    data: function () {
        return {
            page: "Index",
            propPostId: ""
        }
    },
    components: {
        Post,
        WritePost,
        Enter,
        Register,
        Index,
        UsersList,
        Sidebar,
        EditPost
    },
    props: ["posts", "users", "comments"],
    methods: {
        reloadPost: function(post) {
            post["comments"] = Object.values(this.comments)
                .filter(comment => comment.postId === post.id)
                .map(comment => {
                    comment["author"] = this.users[comment.userId].name;
                    return comment;
                });
            post["author"] = this.users[post.userId].name;
            return post;
        }
    },
    computed: {

        viewPosts: function () {
            return Object.values(this.posts).map(this.reloadPost)
                                            .sort((a, b) => b.id - a.id);
        },
        viewUsers: function () {
            return Object.values(this.users);
        },
        viewPost: function() {
            this.reloadPost(this.posts[this.propPostId]);
            console.log("yse");
            return this.posts[this.propPostId];
        }
    }, beforeCreate() {
        this.$root.$on("onChangePage", (page) => this.page = page)
        this.$root.$on("onGotoPost", (postId) => {
            this.page = 'Post';
            this.propPostId = postId;
            console.log("onGotoPost");
        })
    }
}
</script>

<style scoped>

</style>
