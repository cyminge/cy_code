package com.cy.test;

public interface NetConnectInterface {

	/*** 启动socket长连接线程，网络连接和传输操作均需在线程运行后才有效 */
	public void startWork();
	/*** 关闭socket长连接线程 */
	public void stopWork();
	/*** 发送消息 */
	public void sendMessage();
	/*** 判断长连接的状态，是否已建立连接 **/
	public boolean isEstablished();
}
