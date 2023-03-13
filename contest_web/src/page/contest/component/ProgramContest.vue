<template>
  <div class="board">
    <div class="left">
      <t-tag theme="default"  size="large" style="margin-left: 2em;margin-top: 2em;color: #0075FF;background-color: #E3F3FF;">
        编程题
      </t-tag>
      <h3 style="margin-left: 1.6em">{{currentQuestion.currentPaperAns.questionProgramDto.questionTitle}}</h3>
      <div style="width: 90%;margin-left: 1.8em" v-html="currentQuestion.currentPaperAns.questionProgramDto.questionContent"/>
      <p style="font-weight: bold;margin-left: 1.56em;font-size: 18.72px;">输入示例</p>
      <div class="express" v-html="currentQuestion.currentPaperAns.questionProgramDto.questionInputExpress"/>
      <p style="font-weight: bold;margin-left: 1.56em;font-size: 18.72px">输出示例</p>
      <div style="margin-bottom: 5em" class="express" v-html="currentQuestion.currentPaperAns.questionProgramDto.questionOutputExpress"/>
    </div>
    <div class="right">
      <div class="config">
          <div style="display: inline-block;margin-left: 2em;font-size: 16px">编程语言</div>
          <t-select style="display: inline-block;width: 10em;margin-left: 1em;margin-top: 1em" @change="onLanguageChange" v-model="language">
            <t-option value="c">c</t-option>
            <t-option value="cpp">cpp</t-option>
            <t-option value="java">java</t-option>
            <t-option value="python">python</t-option>
            <t-option value="javascript">javascript</t-option>
          </t-select>
          <t-button style="float: right;margin-right: 2em;margin-top: 1em">提交</t-button>
      </div>
      <div class="editor" ref="ace"/>
    </div>
  </div>
</template>

<script>
import ace from "ace-builds";
// import 'ace-builds/webpack-resolver' // 在 webpack 环境中使用必须要导入
import 'ace-builds/src-noconflict/theme-monokai' // 默认设置的主题
import 'ace-builds/src-noconflict/mode-javascript'
import 'ace-builds/src-noconflict/mode-c_cpp'
import 'ace-builds/src-noconflict/mode-java'
import 'ace-builds/src-noconflict/mode-python'
import 'ace-builds/src-noconflict/snippets/javascript'
import 'ace-builds/src-noconflict/snippets/c_cpp'
import 'ace-builds/src-noconflict/snippets/java'
import 'ace-builds/src-noconflict/snippets/python'
import 'ace-builds/src-noconflict/ext-language_tools'

const languageType = {
  java: 'ace/mode/java',
  javascript: 'ace/mode/javascript',
  python: 'ace/mode/python',
  c: 'ace/mode/c_cpp',
  cpp: 'ace/mode/c_cpp'
}

export default {
  name: "ProgramContest",
  props: {
    currentQuestion: {
      required: true
    }
  },
  data() {
    return {
      aceEditor: null,
      themePath: 'ace/theme/monokai', // 不导入 webpack-resolver，该模块路径会报错
      modePath: languageType.cpp, // 同上,
      language: 'cpp',
      code: null
    }
  },
  methods: {
    onLanguageChange: function (value) {
      this.aceEditor.setOption('mode', languageType[value])
    },
    onCodeChange: function () {
      this.code = this.aceEditor.getSession().getValue()
    }
  },
  mounted() {
    this.aceEditor = ace.edit(this.$refs.ace,{
      maxLines: 50,
      minLines: 50,
      fontSize: 14,
      theme: null,
      mode: this.modePath,
      tabSize: 4,
      enableSnippets: true,
      enableLiveAutocompletion: true,
      enableBasicAutocompletion: true
    })
    this.aceEditor.getSession().on('change',this.onCodeChange)
    console.log(this.currentQuestion.currentPaperAns)
  }
    // {
    //   answerId: '',
    //       contestId: '',
    //     questionDetail:{
    //        questionId: '',
    //       questionType: '',
    //       questionContent: '',
    //       questionReferenceAnswer: '',
    //       questionExplain: '',
    //       questionRepoId: '',
    //       createdBy: '',
    //       score: '',
    //       questionOption: ''
    // },
    //   answerContent: '',
    //       createdBy: '',
    //     questionType: ''
    // }
}
</script>

<style scoped>
  .board {
    background-color: white;
    width: 86em;
    height: 54.4em;
    margin-bottom: 5em;
    margin-left: 6em;
    margin-top: -8em;
    box-shadow: 0 0 10px 0 rgb(0 0 0 / 10%);
  }
  .left{
    width: 43em;
    height: 100%;
    box-shadow: 1px 0 0 0 rgb(0 0 0 / 10%);
    float: left;
    overflow: auto;
  }
  .right{
    width: 43em;
    height: 100%;
    float: right;
  }
  .express{
    background-color: #000a2008;
    width: 86%;
    margin-left: 2em;
    padding: 1em;
    border-radius: 5px;
  }
  .editor{
    border: #cccccc solid 1px;
    width: 100%;
  }
  .config{
    background-color: #f9f9f9;
    width: 100%;
    height: 4.3em;
    border: #f0f0f0 solid 1px;
  }
</style>