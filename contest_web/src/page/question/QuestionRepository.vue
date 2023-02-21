<template>
  <div class="page">
    <div class="index">
      <t-list style="width: 100%;height: 100%;border-right: 1px inset">
        <t-list-item style="border-bottom: 1px inset;height: 4em">
          <span style="font-weight: bold;margin-left: 1.5em;color: #2c9fe5;font-size: 18px;margin-top: 0.5em">题库</span>
        </t-list-item>

        <div>
          <t-list-item v-for="tag in tagList" :key="tag.questionTagId" class="list-item">
            <span class="list-item-font">{{tag.questionTagName}}</span>
          </t-list-item>
        </div>


        <t-tooltip content="添加书签" theme="light" placement="bottom">
          <t-list-item @click="addQuestionTag" class="ddd">
            <span style="font-weight: bold;margin-left: 1.5em;font-size: 25px">+</span>
          </t-list-item>
        </t-tooltip>
      </t-list>
    </div>
    <div class="content">
      <div class="right-top">
        <t-link variant="text" theme="default" class="right-top-btn">重命名</t-link>
        <t-link variant="text" theme="default" class="right-top-btn">删除</t-link>
        <t-link variant="text" theme="default" class="right-top-btn">查看</t-link>
        <t-link variant="text" theme="default" class="right-top-btn">新建</t-link>
      </div>
      <div class="right-bottom">
        <QuestionSet @on-click="repoSelected = 1" :selected="repoSelected === 1" title="ACM训练赛" type="programing"/>
        <QuestionSet @on-click="repoSelected = 2" :selected="repoSelected === 2" title="GPLT团体程序设计天梯赛模拟题" type="programing"/>
        <QuestionSet @on-click="repoSelected = 3" :selected="repoSelected === 3" title="蓝桥杯模拟赛" type="programing"/>
        <QuestionSet @on-click="repoSelected = 4" :selected="repoSelected === 4" title="第二届数学建模模拟题" type="project"/>
        <QuestionSet @on-click="repoSelected = 5" :selected="repoSelected === 5" title="计算机网络期末考试" type="paper"/>
      </div>
    </div>
  </div>
  <InputDialog
      title="新建标签"
      placeholder="请输入标签名"
      :visible="dialog.addTagInputDialog.visitable"
      @on-close="closeAddTagInputDialog"
      @on-confirm="confirmAddTag"
      :max-character="24"/>
</template>

<script>
import QuestionSet from "@/page/question/component/QuestionSet";
import InputDialog from "@/page/component/dialog/InputDialog";
import {addQuestionTag, getQuestionTag} from "@/api/question";
import {result} from "@/common/request.result";
import {MessagePlugin} from "tdesign-vue-next";

export default {
  name: "QuestionRepository",
  components: {
    InputDialog,
    QuestionSet
  },
  data() {
    return {
      repoSelected: null,
      dialog: {
        addTagInputDialog: {
          visitable: false
        }
      },
      tagList: [{questionTagId:0,questionTagName:'默认'}]
    }
  },
  methods: {
    addQuestionTag: function () {
      this.dialog.addTagInputDialog.visitable = true
    },
    closeAddTagInputDialog: function () {
      this.dialog.addTagInputDialog.visitable = false
    },
    confirmAddTag: function (questionTagName) {
      this.closeAddTagInputDialog()
      if(questionTagName.length > 12){
        MessagePlugin.error('字数不能超过12')
        return
      }
      addQuestionTag({questionTagName}).then(resp => {
        if(resp.data['resultCode'] === result.code.SUCCESS){
          MessagePlugin.success("标签新建成功！")
        }else{
          MessagePlugin.error('系统繁忙!')
        }
      }).catch(() => {
        MessagePlugin.error('系统繁忙!')
      })
    }
  },
  mounted() {
    getQuestionTag(this.tagList)
  }
}
</script>

<style scoped>
  .page{
    width: 76.5em;
    height: 40em;
    margin-left: 11em;
    margin-top: 1em;
    box-shadow: 0 5px 10px 0 rgb(0 0 0 / 10%);
    border-radius: 6px;
    background-color: white;
  }
  .index{
    width: 10%;
    height: 100%;
    /*background-color: red;*/
    display: inline-block;
    /*border-left: #cccccc 1px solid;*/
  }
  .content{
    width: 90%;
    height: 100%;
    float: right;
    /*background-color: #2c9fe5;*/
    display: inline-block;
  }
  .list-item-font{
    text-align: center;
    width: 100%;
    height: 100%;
  }
  .list-item{
    cursor: pointer;
    position: relative;
  }
  .list-item:hover{
    border-left: #2c9fe5 3px solid;
    background-color: #f6f5f7;
    font-weight: bold;
  }
  .ddd{
    border-top: #f6f5f7 1px solid;
    cursor: pointer;
  }
  .ddd:hover{
    background-color: #f6f5f7;
  }
  .right-top{
    /*background-color: black;*/
    width: 100%;
    height: 12.5%;
    border-bottom: #f6f5f7 1px solid;
    display: inline-block;
  }
  .right-bottom{
    /*background-color: red;*/
    width: 100%;
    height: 87.5%;
    display: inline-block;
    padding-left: 3em;
  }
  .right-top-btn{
    float: right;
    margin-top: 2.5em;
    margin-right: 2.5em;
  }
</style>