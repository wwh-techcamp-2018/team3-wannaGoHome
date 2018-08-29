document.addEventListener("DOMContentLoaded", function(evt) {
    const profileNode = $_(".mypage-profile-holder");
    const teamNode = $_(".maypage-team-holder");
    const activitiesNode = $_(".maypage-activity-holder");
    const profileAvatarNode = $_(".profile-avatar-holder");
    const invitationHolder = $_(".mypage-invitation-holder");
    new MyPage(profileNode, profileAvatarNode, teamNode, activitiesNode, invitationHolder);
});