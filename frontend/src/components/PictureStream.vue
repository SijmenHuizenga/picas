<template>
  <div class="photostream-wrapper">
    <div class="day" v-for="day in days" :key="day.date" :style="'top: ' + day.estimatedDistanceFromTop + 'px'">
      {{day.count}}
    </div>
  </div>
</template>

<script lang="ts">
  import {Vue} from 'vue-class-component';

  const targetHeight = 180;
  const rowHeaderHeight = 50;

  const axios = require('axios').default;

  export default class PictureStream extends Vue {
    days : {date :string, count :number, estimatedHeight :number, estimatedDistanceFromTop :number}[] = []

    mounted() {

      axios
        .get('http://localhost:8080/days')
        .then((response: any) => {
          const days : {date :string, count :number}[] = response.data;

          let photoCount = 0;
          days.forEach(day => {
            photoCount += day.count;
          })

          let commulativeHeight = 0;
          days.forEach((day :{date :string, count :number}) => {
            const estimatedHeight = this.estimatedRowHeight(day.count);
            this.days.push({
              date: day.date,
              count: day.count,
              estimatedHeight: estimatedHeight,
              estimatedDistanceFromTop: commulativeHeight})
            commulativeHeight += estimatedHeight;
          });
        })
    }

    estimatedRowHeight(numberOfPictures :number) {
      // Ideally we would use the average aspect ratio for the photoset, however assume
      // a normal landscape aspect ratio of 3:2, then discount for the likelihood we
      // will be scaling down and coalescing.
      const unwrappedWidth = (3 / 2) * numberOfPictures * targetHeight * (7 / 10);
      const rows = Math.ceil(unwrappedWidth / window.innerWidth);
      return rows * targetHeight + rowHeaderHeight;
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
  .day {
    position: absolute;
  }
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
