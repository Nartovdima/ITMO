<template>
    <div class="users datatable">
        <div class="caption">User</div>
        <table>
            <thead>
            <tr>
                <th>Id</th>
                <th>Login</th>
                <th>Name</th>
                <th>Admin Status</th>
            </tr>
            </thead>
            <tbody>
                <tr v-for="user in users" :key="user.id">
                    <td class="user-id">{{ user.id }}</td>
                    <td class="user-login">{{ user.login }}</td>
                    <td class="user-name">{{ user.name }}</td>
                    <td class="admin-status">{{ user.admin }}</td>
                </tr>
            </tbody>
        </table>
    </div>
</template>

<script>
export default {
    name: "UsersList",
    props: ["users"]
}
</script>

<style scoped>

</style>