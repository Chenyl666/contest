<script setup>
import {onBeforeUnmount, shallowRef, onMounted, watch, ref} from 'vue'
import { Editor, Toolbar } from '@wangeditor/editor-for-vue'
import {defineEmits} from "vue";
import {store} from "@/store";
import {defineProps} from "vue";
import {defineExpose} from "vue";

const props = defineProps({
  content: {
    type: String,
    default: null
  },
  height: {
    type: String,
    default: '500px'
  },
  width: {
    type: String,
    default: null
  },
  saveToStore: {
    type: String,
    default: null
  }
})

const emits = defineEmits(['content-change'])

// 编辑器实例，必须用 shallowRef，重要！
const editorRef = shallowRef()

// 内容 HTML
let valueHtml = ref(props.content)
// const valueHtml = toRef(props,"content")

// 模拟 ajax 异步获取内容
onMounted(() => {
})

// 编辑器配置
const editorConfig = {
  placeholder: '请输入内容...',
  MENU_CONF: {
    uploadImage: {
      fieldName: 'file',
      server: 'http://localhost:8080/filesys/upload/fulltext',
      maxFileSize: 10*1024*1024,
      headers: {'token':store.state.token},
      meta: {
        fileName: '',
        publicPerm: true,
        timeLimit: true
      }
    }
  }
}

const handleCreated = (editor) => {
  editorRef.value = editor // 记录 editor 实例，重要！
}

watch(valueHtml, (newValue) => {
  if(props.saveToStore !== null){
    store.commit(props.saveToStore,editorRef.value.getText())
  }
  // alert('change')
  emits('content-change',newValue)
})

const setValueHtml = (text) => {
  valueHtml.value = text
}

defineExpose(setValueHtml)

const onContentChange = () => {
  if(props.saveToStore !== null){
    store.commit(props.saveToStore,editorRef.value.getText())
  }
  // alert('change on')
  emits('content-change',valueHtml)
}

// 组件销毁时，及时销毁编辑器
onBeforeUnmount(() => {
  const editor = editorRef.value
  if (editor == null) return
  editor.destroy()
})
</script>

<template>
  <div :style="{width: props.width,height: props.height}">
    <!-- 工具栏 -->
    <Toolbar
        style="border: #cccccc solid 1px"
        :editor="editorRef"
        :defaultConfig="toolbarConfig"/>
    <!-- 编辑器 -->
<!--    <Editor-->
<!--        v-model="valueHtml"-->
<!--        :defaultConfig="editorConfig"-->
<!--        style="overflow-y: hidden;"-->
<!--        :style="{height: props.height,width: props.width}"-->
<!--        @onCreated="handleCreated"-->
<!--        @change="onContentChange"/>-->
    <Editor
        style="border: #cccccc solid 1px"
        v-model="valueHtml"
        :defaultConfig="editorConfig"
        @onCreated="handleCreated"
        @change="onContentChange"/>
  </div>
</template>

<!-- 别忘了引入样式 -->
<style src="@wangeditor/editor/dist/css/style.css"></style>