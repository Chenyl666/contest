<template>
  <div class="publish">
    <video v-if="imgSrc === null" style="width: 15em" ref="video"></video>
    <canvas style="display: none" id="canvasCamera"></canvas>
    <div v-if="imgSrc" class="img_bg_camera">
      <img :src="imgSrc" class="tx_img" alt=""/>
    </div>
    <br>
    <t-button :disabled="imgSrc === null" theme="default" @click="clearImg">重新拍照</t-button>
<!--    <t-button theme="default" @click="CloseCamera">关闭摄像头</t-button>-->
    <t-button :disabled="imgSrc" @click="setImage">拍照</t-button>
  </div>
</template>

<script>
export default {
  data() {
    return {
      mediaStreamTrack: {},
      video_stream: '', // 视频stream
      imgSrc: null, // 拍照图片
      canvas: null,
      context: null,
    };
  },
  mounted() {
    // 进入页面 自动调用摄像头
    this.getCamera();
  },
  methods: {
    // 调用打开摄像头功能
    getCamera() {
      // 获取 canvas 画布
      this.canvas = document.getElementById('canvasCamera');
      this.context = this.canvas.getContext('2d');
      // 旧版本浏览器可能根本不支持mediaDevices，我们首先设置一个空对象
      if (navigator.mediaDevices === undefined) {
        navigator.mediaDevices = {};
      }
      // 正常支持版本
      navigator.mediaDevices.getUserMedia({video: true})
          .then((stream) => {
            // 摄像头开启成功
            this.mediaStreamTrack = typeof stream.stop === 'function' ? stream : stream.getTracks()[0];
            this.video_stream = stream;
            this.$refs.video.srcObject = stream;
            this.$refs.video.play();
          })
          .catch(err => {
            console.log(err);
          });
    },
    // 拍照 绘制图片
    setImage() {
      // 点击canvas画图
      this.context.drawImage(
          this.$refs.video,
          0,
          0,
          200,
          100,
      );
      // 获取图片base64链接
      const image = this.canvas.toDataURL('image/png');
      this.imgSrc = image;
      console.log('拍照')
      console.log(image)
      // this.$emit('refreshDataList', this.imgSrc);
      this.$emit('show-next');
    },
    // 打开摄像头
    openCamera() {
      this.getCamera();
    },
    clearImg() {
      this.$emit('refresh-camera')
      // this.imgSrc = null
      // this.closeCamera()
      // this.openCamera()
    },
    // 关闭摄像头
    closeCamera() {
      this.$refs.video.srcObject.getTracks()[0].stop();
    },
  },
};
</script>

<style lang="less" scoped>
video {
  width: 100%;
  height: 300px;
}

canvas {
  width: 100%;
  height: 300px;
}

button {
  width: 100px;
  height: 40px;
  position: relative;
  bottom: 0;
  left: 0;
  margin-right: 2em;
  //background-color: rgb(22, 204, 195);
}

.img_bg_camera {
  img {
    margin-top: 3.8em;
    width: 22.5em;
    //width: 300px;
    height: 16.8em;
    margin-bottom: -3.1em;
  }
}

</style>
