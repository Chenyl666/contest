<template>
  <t-tooltip :content="this.title" theme="light" placement="bottom-left">
    <div @dblclick="$emit('on-db-click')"
         @click="$emit('on-click')"
         :class="{highlight: selected}" class="item">
      <ViewModuleIcon v-if="type === 'PAPER'" class="icon"/>
      <LaptopIcon v-if="type === 'PROGRAMMING'" class="icon"/>
      <FileIcon v-if="type === 'PROJECT'" class="icon"/>
      <div style="margin-left: 0.2em;text-align: center">{{getTitle}}</div>
    </div>
  </t-tooltip>
</template>

<script>
import {LaptopIcon, ViewModuleIcon, FileIcon} from "tdesign-icons-vue-next";

export default {
  props: ['title','type','selected'],
  name: "QuestionSet",
  components: {
    LaptopIcon,
    ViewModuleIcon,
    FileIcon
  },
  computed: {
    getTitle: function () {
      if(this.title.length >= 6){
        return this.title.substr(0,4) + '..'
      }
      return this.title
    }
  }
}
</script>

<style scoped>
  .item{
    padding: 1em;
    width: 5em;
    height: 5em;
    display: inline-block;
    cursor: pointer;
    border: white 3px solid;
  }
  .item:hover{
    background-color: #cccccc;
  }
  .icon{
    width: 6em;
    height: 4em;
    margin-left: -0.5em;
    margin-top: -0.5em
  }
  .highlight{
    background-color: #cccccc;
  }
</style>