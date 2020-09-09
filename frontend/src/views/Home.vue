<template>
  <Dashboard>
    <PictureStream />
  </Dashboard>
</template>

<script lang="ts">
import { Options, Vue } from 'vue-class-component'
import PictureStream from '@/components/PictureStream.vue' // @ is an alias to /src

@Options({
  components: {
    PictureStream
  }
})
export default class Home extends Vue {}
</script>
