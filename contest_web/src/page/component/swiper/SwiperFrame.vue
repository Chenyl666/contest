<template>
  <div :style="{ width: '100%',height: '290px' }">
    <t-swiper :duration="300" :interval="2000" navigationType="dots" >
      <t-swiper-item style="cursor: pointer" v-for="picture in this.pictureList" v-bind:key="picture.index">
        <img :style="{height: '290px'}" :src="picture.link" alt="">
      </t-swiper-item>
    </t-swiper>
  </div>
</template>

<script>

export default {
  name: "SwiperFrame",
  props: {
    pictureList: {
      type: Array,
      required: true
    }
  },
  components: {},
  data() {
    return {

    }
  }
}
</script>

<style scoped>

</style>