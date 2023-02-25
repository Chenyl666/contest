<template>
  <t-space direction="vertical" style="margin-top: 1em;width: 100%;">
    <t-collapse :default-value="[1]">
      <t-collapse-panel header="题目">
        <t-collapse default-expand-all>
          <t-collapse-panel class="questionBigTitle" header="单选题">

            <t-list-item class="questionIndex" style="cursor: pointer">
              <span style="font-size: 13px">1. 以下不是树和图...</span>
              <template #action>
                <t-button size="small" theme="default" variant="text" ghost>X</t-button>
              </template>
            </t-list-item>

            <t-list-item class="questionIndex" style="cursor: pointer">
              <span style="font-size: 13px">2. 已知函数f(x)的...</span>
              <template #action>
                <t-button size="small" theme="default" variant="text" ghost>X</t-button>
              </template>
            </t-list-item>
          </t-collapse-panel>

          <t-collapse-panel class="questionBigTitle" header="多选题"> 多选题内容 </t-collapse-panel>
          <t-collapse-panel class="questionBigTitle" header="判断题"> 判断题内容 </t-collapse-panel>
          <t-collapse-panel class="questionBigTitle" header="填空题"> 填空题内容 </t-collapse-panel>
          <t-collapse-panel class="questionBigTitle" header="问答题"> 问答题内容 </t-collapse-panel>
        </t-collapse>
      </t-collapse-panel>
    </t-collapse>
  </t-space>
</template>
<script setup lang="jsx">

</script>
<style lang="less">
.accordion-demo {
  background-color: #f9f9f9;
  background-size: cover;
}
.button-area {
  margin-top: 20px;
  display: flex;
  align-items: center;
  background-size: cover;
}
.questionBigTitle{
  width: 100%;
  background-size: cover;
}
.questionIndex{
  width: 100%;
  margin-left: -2em;
  overflow: hidden;
}
.questionIndex:hover{
  background-color: #a6a6a6;
}
</style>
