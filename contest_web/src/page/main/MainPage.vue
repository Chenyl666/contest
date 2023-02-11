<template>
  <SwiperFrame style="margin-top: -0.7em" :picture-list="swiperPictureLinks"/>
</template>

<script>
import SwiperFrame from "@/page/component/swiper/SwiperFrame";
export default {
  name: "MainPage",
  components: {SwiperFrame},
  data() {
    return {
      swiperPictureLinks: [
        {
          index: 0,
          link: 'http://127.0.0.1:8080/filesys/download/inline/421820543193321472'
        },
        {
          index: 1,
          link: 'http://127.0.0.1:8080/filesys/download/inline/421821355533537280'
        },
        {
          index: 2,
          link: 'http://127.0.0.1:8080/filesys/download/inline/421821515718201344'
        },
        {
          index: 3,
          link: 'http://127.0.0.1:8080/filesys/download/inline/421821868081680384'
        },
        {
          index: 4,
          link: 'http://127.0.0.1:8080/filesys/download/inline/421822051947384832'
        },
      ]
    }
  }
}
</script>

<style scoped>

</style>