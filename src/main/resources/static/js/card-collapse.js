var cardCollapse = `
    <div class="el-card" :class="shadow ? 'is-' + shadow + '-shadow' : 'is-always-shadow'">
        <div
          v-if="$slots.header || header"
          class="el-card__header"
          :class="isCollapseSelf ? 'collapse-icon-right' : 'collapse-icon-down'"
          @click="isCollapseSelf = !isCollapseSelf"
        >
          <slot name="header">{{ header }}</slot>
        </div>
        <div
          class="el-card__body"
          :style="bodyStyle"
          :class="{'is-collapse': isCollapseSelf}"
        >
          <slot />
        </div>
  </div>
`
var demoComponent = Vue.extend({
    template: cardCollapse,
})