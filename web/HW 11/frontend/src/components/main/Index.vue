<template>
    <div class="posts">
        <ViewPost v-for="post in posts" :comments="post.comments" :post="post" :show-comments="false" :key="post.id"/>
    </div>
</template>

<script>

import ViewPost from "@/components/ViewPost.vue";

export default {
    name: "Index",
    components: {ViewPost},
    props: ["posts"]
}


</script>

<style scoped>

</style>
