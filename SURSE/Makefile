JFLAGS = -g
JC = javac
JVM = java
.SUFFIXES: .java .class
.java.class:
	$(JC) $(JFLAGS) $*.java

CLASSES = \
	VMS.java \
	LaunchPage.java \
	AdminCampaignPage.java \
	AdminVoucherPage.java \
	GuestCampaignPage.java \
	GuestVoucherPage.java \
	GuestNotificationPage.java \
	Campaign.java \
	User.java \
	Voucher.java \
	GiftVoucher.java \
	LoyaltyVoucher.java \
	Notification.java \
	ArrayMap.java \
	CampaignVoucherMap.java \
	UserVoucherMap.java \
	Test.java	

MAIN = Test

default: classes

classes: $(CLASSES:.java=.class)

run: $(MAIN).class
	$(JVM) $(MAIN)

clean:
	$(RM) *.class
