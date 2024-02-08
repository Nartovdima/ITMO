<template>
    <div id="app">
        <Header :user="user"/>
        <Middle :posts="posts" :comments="comments" :users="users"/>
        <Footer/>
    </div>
</template>

<script>
import Header from "./components/Header";
import Middle from "./components/Middle";
import Footer from "./components/Footer";
import axios from "axios"

export default {
    name: 'App',
    components: {
        Footer,
        Middle,
        Header
    },
    methods: {
        getPosts: function () {
            axios.get("/api/1/posts").then(response => {
                this.posts = response.data;
            });
        }
    },
    data: function () {
        return {
            user: null,
            posts: [],
            comments: []
        }
    },
    beforeMount() {
        if (localStorage.getItem("jwt") && !this.user) {
            this.$root.$emit("onJwt", localStorage.getItem("jwt"));
        }

        this.getPosts();
    },
    beforeCreate() {
        this.$root.$on("onEnter", (login, password) => {
            if (password === "") {
                this.$root.$emit("onEnterValidationError", "Password is required");
                return;
            }

            axios.post("/api/1/jwt", {
                    login, password
            }).then(response => {
                localStorage.setItem("jwt", response.data);
                this.$root.$emit("onJwt", response.data);
            }).catch(error => {
                this.$root.$emit("onEnterValidationError", error.response.data);
            });
        });

        this.$root.$on("onJwt", (jwt) => {
            localStorage.setItem("jwt", jwt);

            axios.get("/api/1/users/auth", {
                params: {
                    jwt
                }
            }).then(response => {
                this.user = response.data;
                this.$root.$emit("onChangePage", "Index");
            }).catch(() => this.$root.$emit("onLogout"))
        });

        this.$root.$on("onLogout", () => {
            localStorage.removeItem("jwt");
            this.user = null;
        });

        this.$root.$on("onRegister", (login, name, password) => {

            axios.post("/api/1/users", {
                login, name, password
            }).then(() => {
                this.$root.$emit("onEnter", login, password);
            }).catch(error => {
                this.$root.$emit("onRegisterValidationError", error.response.data);
            });
        });

        this.$root.$on("onUpdatePosts", () => {
            this.getPosts();
        });

        this.$root.$on("onGetComments", (postId) => {
            axios.get("/api/1/post/getComments", {
                params: {
                    postId: postId
                }
            }).then(response => {
                this.comments = response.data;
            });
        });

        this.$root.$on("onWritePost", (title, text) => {
            axios.post("/api/1/posts", {
                title, text, jwt: localStorage.getItem("jwt")
            }).then(() => {
                this.$root.$emit("onUpdatePosts");
                this.$root.$emit("onChangePage", "Index");
            }).catch(error => {
                this.$root.$emit("onWritePostValidationError", error.response.data);
            });
        });

        this.$root.$on("onWriteComment", (postId, text) => {
            axios.post("/api/1/post/addComment", {
                postId, text, jwt: localStorage.getItem("jwt")
            }).then(() => {
                this.$root.$emit("onUpdatePosts");
                this.$root.$emit("onGetComments", postId);
                this.$root.$emit("onGotoPost");
            }).catch(error => {
                this.$root.$emit("onWriteCommentValidationError", error.response.data);
            });
        });
    }
}
</script>

<style>
#app {

}
</style>
