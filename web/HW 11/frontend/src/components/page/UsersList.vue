<template>
    <div class="users datatable">
        <div class="caption">User</div>
        <table>
            <thead>
            <tr>
                <th>Id</th>
                <th>Login</th>
                <th>Name</th>
            </tr>
            </thead>
            <tbody>
                <tr v-for="user in users" :key="user.id">
                    <td class="user-id">{{ user.id }}</td>
                    <td class="user-login">{{ user.login }}</td>
                    <td class="user-name">{{ user.name }}</td>
                </tr>
            </tbody>
        </table>
    </div>
</template>

<script>
import axios from "axios"

export default {
    name: "UsersList",
    data: function () {
        return {
            users: []
        }
    },
    beforeMount() {

        axios.get("/api/1/users").then(response => {
            this.users = response.data;
        });
    },
}
</script>

<style scoped>

</style>