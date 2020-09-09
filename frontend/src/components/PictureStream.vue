<template>
  <div class="fav-list">
    <div v-for="picture in pictures" :key="picture.id"
         :style="'flex-grow:' + picture.width*100/picture.height + ';flex-basis:' + picture.width*240/picture.height + 'px;'">
      <img :src="'http://localhost:8080/media/' + picture.filepath"/>
      <i :style="'padding-bottom:' + (picture.height/picture.width*100) + '%'"></i>
    </div>
  </div>


</template>

<script lang="ts">
  import {Vue} from 'vue-class-component';

  const axios = require('axios').default;

  export default class PictureStream extends Vue {
    pictures = []

    mounted() {
      axios
        .get('http://localhost:8080/pictures')
        .then((response: any) => (this.pictures = response.data))
    }


  }
</script>

<style>
  .fav-list{
    display: flex;
    flex-wrap: wrap;
    margin: 2px;
  }
  /* .fav-list::after{
    content: '';
    flex-grow: 999999999;
    order: 999999999;
  } */
  .fav-list > div{
    margin: 2px;
    background-color: violet;
    position: relative;
  }
  .fav-list > div > i {
    display: block;
    background-color: lightblue;
    /* display: none; */
  }
  .fav-list > div > img {
    position: absolute;
    vertical-align: bottom;
    top: 0;
    width: 100%;
    height: 100%;
    object-fit: cover;
  }
  .fav-list > .placeholder{
    flex-grow: 100;
    flex-basis: 240px;
    height: 0;
    margin: 0;
  }
</style>
