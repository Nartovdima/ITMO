<template>
    <footer>
        <a href="/">Ежумж</a> &copy; 3023 by Mike Mirzayanov
        <div>
          Number of users: {{ usersCount }}
        </div>
        <div>
          Number of posts: {{ postsCount }}
        </div>
    </footer>
</template>

<script>
export default {
    name: "Footer",
    props: ["usersCount", "postsCount"]
}
</script>

<style scoped>

</style>