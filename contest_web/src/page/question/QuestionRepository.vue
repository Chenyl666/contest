<template>
  <div class="page">
    <div class="index">
      <t-list style="width: 100%;height: 100%;border-right: 1px inset">
        <t-list-item style="border-bottom: 1px inset;height: 4em">
          <span style="font-weight: bold;margin-left: 1.5em;color: #2c9fe5;font-size: 18px;margin-top: 0.5em">标签</span>
        </t-list-item>
        <div>
          <t-list-item
              :class="{'list-item-selected': tagSelected === tag.questionTagId}"
              @click="changeTag(tag.questionTagId)"
              v-for="tag in tagList"
              :key="tag.questionTagId"
              class="list-item">
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
        <span style="float: left;margin-top: 1.7em;margin-right: 2.5em;font-weight: bold;margin-left: 4em;color: #2c9fe5;font-size: 18px;">题库</span>
        <t-link @click="dialog.confirmDeleteTag.visitable = true" variant="text" theme="default" class="right-top-btn">删除标签</t-link>
        <t-link @click="deleteQuestionRepo" variant="text" theme="default" class="right-top-btn">删除题库</t-link>
        <t-link @click="questionRepoDetail(questionRepoSelected)" variant="text" theme="default" class="right-top-btn">查看题库</t-link>
        <t-link @click="openNewRepoDialog" variant="text" theme="default" class="right-top-btn">新建题库</t-link>
      </div>
      <div class="right-bottom" @click.capture="questionRepoSelected = null">
        <QuestionSet v-for="questionRepo in questionRepoList"
                     :key="questionRepo.questionRepoId"
                     @on-click="questionRepoSelected = questionRepo.questionRepoId"
                     @on-db-click="questionRepoDetail(questionRepo.questionRepoId)"
                     :selected="questionRepoSelected === questionRepo.questionRepoId"
                     :title="questionRepo.questionRepoName"
                     :type="questionRepo.questionRepoType"/>
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
  <NewRepoDialog
      @on-close="closeNewRepoDialog"
      @on-confirm="confirmNewRepoDialog"
      :visible="dialog.newRepositoryDialog.visitable"/>
  <ConfirmDialog
      @on-close="dialog.confirmDeleteRepo.visitable = false"
      @on-confirm="confirmDeleteRepo"
      content="确定要删除该题库吗？"
      :is-visible="dialog.confirmDeleteRepo.visitable"
      title="提示"/>
  <ConfirmDialog
      @on-close="dialog.confirmDeleteTag.visitable = false"
      @on-confirm="confirmDeleteQuestionTag"
      :content="`确定要删除该书签 ' ${getCurrentQuestionTag} ' 吗？（其中的所有题目也将会被删除）`"
      :is-visible="dialog.confirmDeleteTag.visitable"
      title="提示"/>
</template>

<script>
import QuestionSet from "@/page/question/component/QuestionSet";
import InputDialog from "@/page/component/dialog/InputDialog";
import {
  addQuestionRepo,
  addQuestionTag,
  deleteQuestionRepo,
  deleteQuestionTag,
  getQuestionRepo,
  getQuestionTag
} from "@/api/question";
import {result} from "@/common/request.result";
import {MessagePlugin} from "tdesign-vue-next";
import NewRepoDialog from "@/page/question/component/NewRepoDialog";
import ConfirmDialog from "@/page/component/dialog/ConfirmDialog";
import router from "@/router/router";

export default {
  name: "QuestionRepository",
  components: {
    ConfirmDialog,
    NewRepoDialog,
    InputDialog,
    QuestionSet
  },
  data() {
    return {
      dialog: {
        addTagInputDialog: {
          visitable: false
        },
        newRepositoryDialog: {
          visitable: false
        },
        confirmDeleteRepo: {
          visitable: false
        },
        confirmDeleteTag: {
          visitable: false
        }
      },
      tagList: [{questionTagId:0,questionTagName:'默认'}],
      tagSelected: null,
      questionRepoList: [],
      questionRepoSelected: null
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
          this.tagList.push({questionTagId: resp.data.data,questionTagName})
          MessagePlugin.success("标签新建成功！")
          this.changeTag(resp.data.data)
        }else{
          MessagePlugin.error('系统繁忙!')
        }
      }).catch(() => {
        MessagePlugin.error('系统繁忙!')
      })
    },
    changeTag: function (questionTagId) {
      this.tagSelected = questionTagId
      getQuestionRepo(questionTagId,this.questionRepoList)
      this.questionRepoSelected = null
    },
    closeNewRepoDialog: function () {
      this.dialog.newRepositoryDialog.visitable = false
    },
    confirmNewRepoDialog: function (formData) {
      this.closeNewRepoDialog()
      let questionRepoDto = {
        questionRepoId: null,
        questionRepoName: formData.input,
        questionRepoType: formData.option,
        questionTagId: this.tagSelected
      }
      addQuestionRepo(questionRepoDto).then(resp => {
        if(resp.data['resultCode'] === result.code.SUCCESS){
          questionRepoDto.questionRepoId = resp.data.data
          this.questionRepoList.push(questionRepoDto)
        }
        if(resp.data['resultCode'] === result.code.FAIL){
          MessagePlugin.error('该题库已存在！')
        }
      })
    },
    openNewRepoDialog: function () {
      this.dialog.newRepositoryDialog.visitable = true
    },
    questionRepoDetail: function (questionRepoId) {
      router.push('/question/repo/detail/'.concat(questionRepoId))
    },
    deleteQuestionRepo: function () {
      this.dialog.confirmDeleteRepo.visitable = true
    },
    confirmDeleteRepo: function () {
      deleteQuestionRepo(this.questionRepoSelected).then(resp => {
        if(resp.data.resultCode === result.code.SUCCESS){
          MessagePlugin.success('删除成功')
          getQuestionRepo(this.tagSelected, this.questionRepoList)
          this.questionRepoSelected = null
        }
        this.dialog.confirmDeleteRepo.visitable = false
      })
    },
    confirmDeleteQuestionTag: function () {
      this.dialog.confirmDeleteTag.visitable = false
      deleteQuestionTag(this.tagSelected).then(async resp => {
        if (resp.data.resultCode === result.code.SUCCESS) {
          MessagePlugin.success('删除成功')
          await getQuestionTag(this.tagList)
          this.tagSelected = this.tagList[0].questionTagId
          await getQuestionRepo(this.tagSelected, this.questionRepoList)
          this.questionRepoSelected = null
        }
      })
    }
  },
  computed: {
    getCurrentQuestionTag: function () {
      for(let index = 0;index < this.tagList.length; index++){
        if(this.tagList[index].questionTagId === this.tagSelected){
          return this.tagList[index].questionTagName
        }
      }
      return null
    }
  },
  async mounted() {
    await getQuestionTag(this.tagList)
    this.tagSelected = this.tagList[0].questionTagId
    await getQuestionRepo(this.tagSelected, this.questionRepoList)
    this.questionRepoSelected = null
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
  .list-item-selected{
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