<template>
  <div class="top">
    <t-form>
      <t-link @click="addQuestion" theme="primary" style="display: block;width: 5em;margin-left: 4em;margin-top: 1em">添加编程题</t-link>
      <t-form-item label="题目标题：" label-align="top" style="display: inline-block;margin-top: 2.3em;margin-left: 4em">
        <t-input v-model="question.questionTitle" style="width: 20em" placeholder="请输入标题"/>
      </t-form-item>
      <t-form-item label="分值：" label-align="top" style="display: inline-block;margin-left: 4em">
        <t-input disabled style="width: 20em" v-model="question.score">
          <template #suffix-icon>
            <t-tooltip content="分值会通过测试用例的数量来确定，为测试用例数量的5倍" theme="light">
              <HelpCircleIcon style="margin-left: 1em;cursor: pointer"/>
            </t-tooltip>
          </template>
        </t-input>
      </t-form-item>
      <br>
      <t-form-item label="时间限制：" label-align="top" style="display: inline-block;margin-left: 4em">
        <t-input v-model="question.timeLimit" style="width: 20em" placeholder="单位：毫秒"/>
      </t-form-item>

      <t-form-item label="内存限制：" label-align="top" style="display: inline-block;margin-left: 4em">
        <t-input v-model="question.memoryLimit" style="width: 20em" placeholder="单位：KB"/>
      </t-form-item>


    </t-form>
  </div>
  <div class="bottom" :key="question.key">
    <div class="editor">
      <h2 class="editor-title">题目描述</h2>
      <MyEditor :content="question.questionContent" width="90%" style="margin-left: 4em;" height="300px" @content-change="v => question.questionContent = v"/>
    </div>
    <div class="editor">
      <h2 class="editor-title">输入格式</h2>
      <MyEditor :content="question.questionInputExpress" width="90%" style="margin-left: 4em;" height="300px" @content-change="v => question.questionInputExpress = v"/>
    </div>
    <div class="editor">
      <h2 class="editor-title">输出格式</h2>
      <MyEditor :content="question.questionOutputExpress" width="90%" style="margin-left: 4em;" height="300px" @content-change="v => question.questionOutputExpress = v"/>
    </div>
    <div style="margin-left: 4em">
      <h2 style="display: inline-block">上传测试用例</h2>
      <t-tooltip content="每个测试用例必须有输入和输出，至少要有一个测试用例，测试用例以文本形式上传，判题时严格按照输入输出标准和文件进行对比。" theme="light">
        <HelpCircleIcon style="margin-left: 1em;cursor: pointer;margin-top: -0.3em;width: 1.3em;height: 1.3em"/>
      </t-tooltip>
      <div>
          <t-list style="width: 95%">
            <t-list-item :key="index" v-for="(questionExample,index) in question.questionExampleList">
              测试用例{{index}}
              <template #action>
                <div style="display: inline-block" v-if="!questionExample.inputExampleUploadSuccess || !questionExample.outputExampleUploadSuccess">
                  <t-badge :count="questionExample.inputExampleUploadSuccess?'✔':''" color="#00A870" :offset="[22, 20]">
                    <t-button style="margin-right: 1em" @click="onExampleBtnUploadClick(index,`in`)">上传输入</t-button>
                    <!--                  <UploadButton @on-success="(url) => onInputExampleUploadSuccess(url,index)" value="上传输入"/>-->
                  </t-badge>
                  <t-badge :count="questionExample.outputExampleUploadSuccess?'✔':''" color="#00A870" :offset="[10, 20]">
                    <t-button @click="onExampleBtnUploadClick(index,`out`)">上传输出</t-button>
                    <!--                  <UploadButton @on-success="(url) => onOutputExampleUploadSuccess(url,index)" value="上传输出" style="margin-left: 16px"/>-->
                  </t-badge>
                </div>
                <div style="display: inline-block" v-else>
                  <t-button @click="showExample(index)" theme="success">查看用例</t-button>
                </div>
                <t-button :disabled="question.questionExampleList.length === 1" @click="onRemoveTestExample(index)" theme="danger" hover="color" style="margin-left: 16px"> 删除用例 </t-button>
              </template>
            </t-list-item>
            <t-list-item @click="onAddTestExample" class="addOption" style="cursor: pointer">
              <div style="margin-left: 25em;font-weight: bold;font-size: 20px">添加</div>
            </t-list-item>
          </t-list>
      </div>
    </div>
    <t-button @click="save" size="large" style="margin-top: 5em;margin-left: 4em;width: 90%" theme="primary">保存</t-button>
  </div>
  <ExampleDialog :visible="exampleDialog.visitable" @on-success="onConfirmUploadExample" @on-close="exampleDialog.visitable = false"/>
  <ShowExampleDialog :input-url="showExampleDialog.inputUrl"
                     :output-url="showExampleDialog.outputUrl"
                     :visitable="showExampleDialog.visitable"
                     @on-close="showExampleDialog.visitable = false"/>
</template>

<script>
import {HelpCircleIcon} from "tdesign-icons-vue-next";
import MyEditor from "@/page/component/editor/MyEditor";
// import UploadButton from "@/page/component/upload/UploadButton";
import {deleteProgramExample, getQuestionProgramById, saveQuestionProgram} from "@/api/question";
import {PROGRAM_EXAMPLE_TYPE} from "@/common/question";
import {result} from "@/common/request.result";
import {MessagePlugin} from "tdesign-vue-next";
import ExampleDialog from "@/page/question/component/dialog/ExampleDialog";
import {uploadText} from "@/api/file_upload.";
import ShowExampleDialog from "@/page/contest/component/ShowExampleDialog";
import {getUrlText} from "@/api/url_text";

export default {
  name: "ProgrammingQuestionContent",
  // components: {UploadButton, MyEditor, HelpCircleIcon},
  components: {ShowExampleDialog, ExampleDialog, MyEditor, HelpCircleIcon},
  data() {
    return {
      exampleDialog:{
        visitable: false,
        index: null,
        type: null,
        text: null
      },
      showExampleDialog: {
        inputUrl: null,
        outputUrl: null,
        visitable: false
      },
      question: {
        load: false,
        score: 5,
        key: 0,
        questionId: null,
        questionTitle: null,
        questionContent: null,
        questionInputExpress: null,
        questionOutputExpress: null,
        timeLimit: null,
        memoryLimit: null,
        questionExampleList: [
            {
              inputExampleUrl: null,
              outputExampleUrl: null,
              inputExampleUploadSuccess: false,
              outputExampleUploadSuccess: false,
            }
        ],
      },
    }
  },
  methods: {
    async showExample(index) {
      await getUrlText(this.question.questionExampleList[index].inputExampleUrl.replace('contest-filesys', '127.0.0.1:8080')).then(resp => {
        this.showExampleDialog.inputUrl = resp.data
      })
      await getUrlText(this.question.questionExampleList[index].outputExampleUrl.replace('contest-filesys', '127.0.0.1:8080')).then(resp => {
        this.showExampleDialog.outputUrl = resp.data
      })
      this.showExampleDialog.visitable = true
    },
    onConfirmUploadExample: async function (text) {
      this.exampleDialog.visitable = false
      let url = ''
      await uploadText(text, this.exampleDialog.type + this.exampleDialog.index).then(res => {
        url = res.replace("127.0.0.1:8080",'contest-filesys')
      }).catch(async () => {
        await MessagePlugin.error('上传失败！')
      })
      if(this.exampleDialog.type === 'in'){
        this.onInputExampleUploadSuccess(url,this.exampleDialog.index)
        await MessagePlugin.success('上传成功！')
      }else{
        this.onOutputExampleUploadSuccess(url,this.exampleDialog.index)
        await MessagePlugin.success("上传成功")
      }
    },
    onExampleBtnUploadClick: function (index,type) {
      this.exampleDialog.visitable = true
      this.exampleDialog.index = index
      this.exampleDialog.type = type
    },
    onAddTestExample: function () {
      this.question.questionExampleList.push({
        inputExampleUrl: '',
        outputExampleUrl: '',
        inputExampleUploadSuccess: false,
        outputExampleUploadSuccess: false
      })
      this.question.score += 5
    },
    onRemoveTestExample: function (index) {
      this.question.questionExampleList.splice(index,1)
      this.question.score -= 5
      if(this.question.load){
        deleteProgramExample(this.question.questionId,index)
      }
    },
    onInputExampleUploadSuccess: function (url, index) {
      this.question.questionExampleList[index].inputExampleUploadSuccess = true
      this.question.questionExampleList[index].inputExampleUrl = url
    },
    onOutputExampleUploadSuccess: function (url, index) {
      this.question.questionExampleList[index].outputExampleUploadSuccess = true
      this.question.questionExampleList[index].outputExampleUrl = url
    },
    save: function () {
      let programExampleList = []
      for(let i=0;i<this.question.questionExampleList.length;i++){
        programExampleList.push({
          questionId: this.question.questionId,
          exampleNumber: i,
          exampleType: PROGRAM_EXAMPLE_TYPE.INPUT_EXAMPLE,
          exampleUrl: this.question.questionExampleList[i].inputExampleUrl
        })
        programExampleList.push({
          questionId: this.question.questionId,
          exampleNumber: i,
          exampleType: PROGRAM_EXAMPLE_TYPE.OUTPUT_EXAMPLE,
          exampleUrl: this.question.questionExampleList[i].outputExampleUrl
        })
      }
      saveQuestionProgram({
        questionId: this.question.questionId,
        questionRepoId: this.$route.params.questionRepoId,
        questionTitle: this.question.questionTitle,
        questionContent: this.question.questionContent,
        timeLimit: this.question.timeLimit,
        memoryLimit: this.question.memoryLimit,
        questionInputExpress: this.question.questionInputExpress,
        questionOutputExpress: this.question.questionOutputExpress,
        score: this.question.score,
        programExampleList
      }).then(resp => {
        if(resp.data.resultCode === result.code.SUCCESS){
          MessagePlugin.success('保存成功！')
          this.$emit('load-question')
        }else{
          MessagePlugin.error('系统繁忙！')
        }
      }).catch(() => {
        MessagePlugin.error('系统繁忙！')
      })
    },
    loadQuestion: function (questionId) {
      getQuestionProgramById(questionId).then(resp =>{
        if(resp.data.resultCode === result.code.SUCCESS){
          let data = resp.data.data
          this.question.score = data.score
          this.question.questionId = questionId
          this.question.questionTitle = data.questionTitle
          this.question.questionContent = data.questionContent
          this.question.questionInputExpress = data.questionInputExpress
          this.question.questionOutputExpress = data.questionOutputExpress
          this.question.timeLimit = data.timeLimit
          this.question.memoryLimit = data.memoryLimit
          this.question.load = true
          let map = {}
          data.programExampleList.forEach(programExample => {
            if(map[programExample.exampleNumber] === undefined){
              map[programExample.exampleNumber] = {exampleNumber: programExample.exampleNumber}
            }
            if(programExample.exampleType === PROGRAM_EXAMPLE_TYPE.INPUT_EXAMPLE){
              map[programExample.exampleNumber]['inputExampleUrl'] = programExample.exampleUrl
              map[programExample.exampleNumber]['inputExampleUploadSuccess'] = true
            }else if(programExample.exampleType === PROGRAM_EXAMPLE_TYPE.OUTPUT_EXAMPLE){
              map[programExample.exampleNumber]['outputExampleUrl'] = programExample.exampleUrl
              map[programExample.exampleNumber]['outputExampleUploadSuccess'] = true
            }
          })
          this.question.questionExampleList.length = 0
          for(let key in map){
            this.question.questionExampleList.push(map[key])
          }
          this.question.key++
        }
      })
    },
    addQuestion: function () {
      this.question.load = false
      this.question.score = 5
      this.question.questionId = null
      this.question.questionTitle = null
      this.question.questionContent = null
      this.question.questionInputExpress = null
      this.question.questionOutputExpress = null
      this.question.timeLimit = null
      this.question.memoryLimit = null
      this.question.questionExampleList = [{
        inputExampleUrl: null,
        outputExampleUrl: null,
        inputExampleUploadSuccess: false,
        outputExampleUploadSuccess: false,
      }]
      this.question.key = (this.question.key+1)%5000
    },
  }
}
</script>

<style scoped>
  .top{
    width: 100%;
    margin-left: 0.5em;
    /*background-color: #2c9fe5;*/
  }
  .bottom{
    width: 100%;
    margin-bottom: 30em;
  }
  .editor{
    margin-bottom: 8em;
  }
  .editor-title{
    margin-left: 2.7em
  }
  .addOption:hover{
    background-color: #cccccc;
  }
</style>