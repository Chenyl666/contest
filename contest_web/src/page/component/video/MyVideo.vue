<template>
  <div>
    <video id="vPull"
           autoplay
           muted
    style="width: 40em"/>
  </div>
</template>

<script>
import flv from "flv.js";

export default {
  name: "MyVideo",
  data() {
    return {
      player: null,
    };
  },
  methods: {
    play(urls) {
      let video = document.getElementById("vPull"); //定义播放路径
      video.volume = 0.2
      // alert(video)
      if (flv.isSupported()) {
        this.player = flv.createPlayer(
            {
              type: "flv",
              isLive: true,
              url: urls,
            },
            {
              enableWorker: false, //不启用分离线程
              enableStashBuffer: false, //关闭IO隐藏缓冲区
              isLive: true,
              lazyLoad: false,
            }
        );
      } else {
        console.log("不支持的格式");
        return;
      }
      this.player.attachMediaElement(video);
      this.player.load();
      this.player.play();
    },
    destruction() {
      //销毁对象
      if (this.player) {
        this.player.pause();
        this.player.destroy();
        this.player = null;
      }
    },
  },
  created() {
    this.$nextTick(() => {
      this.play('http://127.0.0.1:1936/live?app=contest&port=1935&stream=example');
    })
  }
};
</script>


<style lang="less" scoped>
  .video{

  }
</style>

