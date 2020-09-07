<template>
  <Dashboard>
    Content Content Content ContentContent ContentContent ContentContent ContentContent ContentContent ContentContent ContentContent ContentContent ContentContent Content
  </Dashboard>
</template>

<script lang="ts">
import { Options, Vue } from 'vue-class-component'
import Dashboard from '@/components/Dashboard.vue' // @ is an alias to /src

@Options({
  components: {
    Dashboard
  }
})
export default class Home extends Vue {}
</script>
