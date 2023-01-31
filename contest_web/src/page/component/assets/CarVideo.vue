<template>
  <div class="lottieMain">
    <div id="imgWrapper"></div>
  </div>
</template>

<script>
import lottie from 'lottie-web'
import {reactive} from "vue";
export default {
  name: "CarVideo",
  mounted() {
    let AstronautDefalut = require('../../../assets/car-loading4-data.json');
    let AstronautJSON = reactive(JSON.parse(JSON.stringify(AstronautDefalut)));   //深拷贝json
    AstronautJSON.assets.forEach(item=>{ //更改json里面的图片路径
      item.u="";
      if(item.w && item.h && !item.p.startsWith('data:image')){
        item.p = require(`../../../assets/${item.p}`);  //这里是因为图片如果不是放在根目录下，就要把图片单独引入；
      }
      return item;
    })
    let amin = lottie.loadAnimation({
      container: document.getElementById('imgWrapper'),
      renderer: 'svg',
      loop: true,
      autoplay: true,
      animationData: AstronautJSON
    })
    amin.addEventListener('complete', () => {
      //结束动画
      console.log("动画结束")
    })
  }
}
</script>

<style scoped>

</style>