<template>
    <div class="middle">
        <Sidebar :posts="viewPosts.slice(0, 2)"/>
        <main>
            <Index v-if="page === 'Index'" :posts="viewPosts"/>
            <Enter v-if="page === 'Enter'"/>
            <Register v-if="page === 'Register'"/>
            <UsersList v-if="page === 'UsersList'"/>
            <WritePost v-if="page === 'WritePost'"/>
            <Post v-if="page === 'Post'" :postId="propPostId" :comments="comments"/>
        </main>
    </div>
</template>

<script>
import Sidebar from "./sidebar/Sidebar";
import Index from "./main/Index";
import Enter from "./main/Enter";
import Register from "./main/Register";
import UsersList from "./page/UsersList";
import WritePost from "./page/WritePost";
import Post from "./page/Post";

export default {
    name: "Middle",
    data: function () {
        return {
            page: "Index",
            propPostId: ""
        }
    },
    components: {
        Register,
        Enter,
        Index,
        Sidebar,
        UsersList,
        WritePost,
        Post
    },
    props: ["posts", "comments"],
    computed: {
        viewPosts: function () {
            return Object.values(this.posts).sort((a, b) => b.id - a.id);
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
