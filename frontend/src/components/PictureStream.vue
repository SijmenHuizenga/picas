<template>
  <div class="photogrid" v-for="section in sections" :key="section.date">
    <div v-for="picture in section.pictures" :key="picture.id"
         :style="'flex-grow:' + picture.width*100/picture.height + ';flex-basis:' + picture.width*240/picture.height + 'px;'">
      <img :src="'http://localhost:8080/media/' + picture.filepath" :alt="picture.filepath"/>
      <i :style="'padding-bottom:' + (picture.height/picture.width*100) + '%'"></i>
    </div>
  </div>
</template>

<script lang="ts">
  import {Vue} from 'vue-class-component';

  const axios = require('axios').default;

  export default class PictureStream extends Vue {
    allDays = []
    sections : {date :string, pictures :{filepath :string, width :number, height :number}[]}[] = []

    mounted() {
      axios
        .get('http://localhost:8080/days')
        .then((response: any) => {
          this.allDays = response.data;
          this.allDays.forEach((day :{date :string, count :number}) => {
            axios.get('http://localhost:8080/day/'+day.date)
                 .then((response :any) => {
                   this.sections.push({'date': day.date, 'pictures': response.data})
                 })
          })
        })
    }

    created () {
      window.addEventListener('scroll', this.handleScroll);
    }

    destroyed () {
      window.removeEventListener('scroll', this.handleScroll);
    }

    handleScroll (event :any) {
      // Any code to be executed when the window is scrolled
    }


  }
</script>

<style>
  .photogrid {
    display: flex;
    flex-wrap: wrap;
    margin: 2px;
  }

  .photogrid::after {
    content: '';
    flex-grow: 999999999;
    order: 999999999;
  }

  .photogrid > div {
    margin: 2px;
    background-color: violet;
    position: relative;
  }

  .photogrid > div > i {
    display: block;
    background-color: lightblue;
    /* display: none; */
  }

  .photogrid > div > img {
    position: absolute;
    vertical-align: bottom;
    top: 0;
    width: 100%;
    height: 100%;
    object-fit: cover;
  }

  .photogrid > .placeholder {
    flex-grow: 100;
    flex-basis: 240px;
    height: 0;
    margin: 0;
  }
</style>
