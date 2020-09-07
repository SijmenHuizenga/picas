<template>
  <div class="navbar-expand-sm">
    <nav class="navbar sticky-top p-0">
      <div class="col-md-2"/>
      <div class="form-inline flex-fill">
        <input class="form-control nav-search" type="text" placeholder="Search" aria-label="Search">
      </div>
      <ul class="navbar-nav px-3">
        <li class="nav-item">
          <a class="nav-link text-secondary" href="#">Upload</a>
        </li>
      </ul>
    </nav>
  </div>

  <div class="container-fluid">
    <Sidebar />
    <slot></slot>
  </div>
</template>

<script lang="ts">
  import { Options, Vue } from 'vue-class-component'
  import Sidebar from '@/components/Sidebar.vue' // @ is an alias to /src

  @Options({
    components: {
      Sidebar
    }
  })
  export default class Home extends Vue {}
</script>

<style lang="scss">
  body {
    font-size: .875rem;
  }

  .container-fluid {
    padding: 0;
  }

  .navbar {
    box-shadow: 0 0 4px 1px rgba(0, 0, 0, 0.1)
  }

  .navbar .navbar-nav {
    padding-top: .4rem;
    padding-bottom: .4rem;
  }

  .nav-search {
    color: #000;
    background-color: rgba(0, 0, 0, .05);
    border-color: rgba(255, 255, 255, .1);
    border-radius: 8px;
  }

  @media (min-width: 768px) {
  .nav-search {
      min-width: 350px;
    }
  }

  .nav-search::placeholder {
    color: rgba(0, 0, 0, .45);
  }

  .nav-search:focus {
    border-color: transparent;
    box-shadow: 0 0 0 3px rgba(255, 255, 255, .25);
    background-color: rgba(0, 0, 0, .05);
  }

</style>
