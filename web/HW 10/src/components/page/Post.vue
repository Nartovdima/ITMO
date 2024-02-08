<template>
    <div>
        <ViewPost :post="post" :comments="comments" :showComments="true"/>
        <div class="form">
            <div class="header">Write Comment</div>
            <div class="body">
                <form @submit.prevent="onWriteComment">
                    <div class="field">
                        <div class="name">
                            <label for="text">Text:</label>
                        </div>
                        <div class="value">
                            <textarea id="text" name="text" v-model="text"></textarea>
                        </div>
                    </div>
                    <div class="error">{{ error }}</div>
                    <div class="button-field">
                        <input type="submit" value="Write">
                    </div>
                </form>
            </div>
        </div>
    </div>
</template>

<script>
import ViewPost from "@/components/ViewPost.vue";

export default {
    name: "Post",
    components: {ViewPost},
    props: ["post", "comments"],
    data: function () {
        return {
            postId: "",
            text: "",
            error: ""
        }
    },
    methods: {
        onWriteComment: function () {
            this.error = "";
            this.$root.$emit("onWriteComment", this.post.id, this.text);
            console.log(this.post);
            this.$root.$on("onGotoPost", this.post.id);
            this.text = "";
        }
    },
    beforeMount() {
        this.$root.$on("onWriteCommentValidationError", error => this.error = error);

    }
}
</script>

<style scoped>

</style>